<template>
  <div id="dailyAverageChart">
    <h2 class="chart-title">每天平均数据</h2>
    <div class="input-container">
      <a-select
          v-model:value="selectedMonth"
          placeholder="选择月份"
          style="width: 120px; margin-right: 8px"
      >
        <a-select-option v-for="month in 12" :key="month" :value="month">
          {{ month }}月
        </a-select-option>
      </a-select>
      <a-button type="primary" @click="fetchData">查询</a-button>
    </div>

    <a-spin :spinning="chartLoading" tip="图表数据加载中...">
      <a-empty
          v-if="isEmpty && !chartLoading"
          description="暂无数据"
          class="chart-empty"
      />
      <div v-show="!isEmpty && !chartLoading" ref="chartRef" class="chart"></div>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import {ref, onMounted, watch, nextTick} from 'vue';
import * as echarts from 'echarts';
import {getByMonthUsingGet} from "@/api/weatherBasicAnalysisController.ts";
import {type WeatherDataType, WeatherDataTypeLabel} from "@/constant/WeatherDataType.ts";
import dayjs from "dayjs";
import {message} from "ant-design-vue";

const props = defineProps<{
  year: dayjs.Dayjs | null;
  type: WeatherDataType;
}>();

const selectedMonth = ref<number>(dayjs().month() + 1);
const chartRef = ref<HTMLDivElement | null>(null);
let chartInstance: echarts.ECharts | null = null;

const initChart = () => {
  if (chartRef.value && !chartInstance) {
    chartInstance = echarts.init(chartRef.value);
    window.addEventListener('resize', resizeChart);
  }
};

const resizeChart = () => {
  chartInstance?.resize();
};

const chartLoading = ref(false);
const isEmpty = ref(false);

const fetchData = async () => {
  if (!props.year || !selectedMonth.value) {
    message.warning('请选择年份和月份');
    return;
  }

  try {
    chartLoading.value = true;
    const year = props.year.format('YYYY');
    const month = selectedMonth.value.toString().padStart(2, '0');

    const res = await getByMonthUsingGet({
      year,
      month,
      type: props.type
    });

    if (res.data.code === 0) {
      isEmpty.value = res.data.data.length === 0;
      if (!isEmpty.value) {
        await nextTick(); // 确保DOM更新完成
        updateChart(res.data.data);
      } else {
        clearChart();
      }
    } else {
      message.error(res.data.message || '获取数据失败');
      isEmpty.value = true;
      clearChart();
    }
  } catch (error) {
    console.error('获取数据失败:', error);
    message.error('请求失败，请稍后重试');
    isEmpty.value = true;
    clearChart();
  } finally {
    chartLoading.value = false;
  }
};

const clearChart = () => {
  if (chartInstance) {
    chartInstance.clear();
  }
};

const updateChart = (data: Array<{ label: string; value: any }>) => {
  if (!chartInstance) {
    initChart();
    if (!chartInstance) return;
  }

  // 确保数据排序正确（按日期）
  const sortedData = [...data].sort((a, b) => {
    const dayA = parseInt(a.label.replace('日', ''));
    const dayB = parseInt(b.label.replace('日', ''));
    return dayA - dayB;
  });
  const typeName = WeatherDataTypeLabel[props.type];

  const option = {
    title: {
      text: `${props.year?.format('YYYY')}年${selectedMonth.value}月每天平均${typeName}数据`,
      left: 'center',
      textStyle: {
        fontSize: 16
      }
    },
    tooltip: {
      trigger: 'axis',
      formatter: (params: any) => {
        return `${params[0].axisValue}<br/>${params[0].marker} ${params[0].seriesName}: ${params[0].data}${getUnitByType(props.type)}`;
      }
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: sortedData.map(item => item.label),
      axisLabel: {
        interval: 0,
        rotate: 30 // 标签旋转防止重叠
      }
    },
    yAxis: {
      type: 'value',
      name: getUnitByType(props.type),
      axisLabel: {
        formatter: `{value} ${getUnitByType(props.type)}`
      }
    },
    series: [{
      name: '平均' + typeName,
      type: 'line',
      data: sortedData.map(item => item.value),
      itemStyle: {
        color: '#13c2c2'
      },
      smooth: true,
      symbol: 'circle',
      symbolSize: 8,
      lineStyle: {
        width: 3
      },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          {offset: 0, color: 'rgba(19, 194, 194, 0.5)'},
          {offset: 1, color: 'rgba(19, 194, 194, 0.1)'}
        ])
      },
      markPoint: {
        data: [
          {type: 'max', name: '最大值'},
          {type: 'min', name: '最小值'}
        ]
      }
    }]
  };

  chartInstance.clear();
  chartInstance.setOption(option);
  setTimeout(() => {
    chartInstance?.resize();
  }, 0);
};

// 根据数据类型获取单位
const getUnitByType = (type: WeatherDataType): string => {
  const units = {
    TEMPERATURE: '°C',
    HUMIDITY: '%',
    PRESSURE: 'hPa',
    WIND_SPEED: 'm/s',
    WIND_DIRECTION: '°',
    RAINFALL: 'mm'
  };
  return units[type] || '';
};

onMounted(() => {
  initChart();
  fetchData();
});

watch(() => [props.year, props.type, selectedMonth.value], () => {
  fetchData();
});

defineExpose({
  fetchData
});
</script>

<style scoped>
#dailyAverageChart {
  width: 100%;
  background: #fff;
  padding: 16px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

#dailyAverageChart .chart-title {
  text-align: center;
  margin-bottom: 16px;
  font-size: 18px;
  font-weight: 500;
  color: rgba(0, 0, 0, 0.85);
}

#dailyAverageChart .input-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 16px;
  gap: 8px;
}

#dailyAverageChart .chart {
  width: 100%;
  height: 400px;
  min-height: 400px;
}

#dailyAverageChart .chart-empty {
  padding: 40px 0;
  text-align: center;
  background: #fff;
  border-radius: 4px;
}
</style>