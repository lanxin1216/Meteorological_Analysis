<template>
  <div id="hourlyDataChart">
    <h2 class="chart-title">指定日期数据</h2>
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
      <a-select
          v-model:value="selectedDay"
          placeholder="选择日期"
          style="width: 120px; margin-right: 8px"
      >
        <a-select-option v-for="day in daysInMonth" :key="day" :value="day">
          {{ day }}日
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
import {ref, onMounted, watch, computed} from 'vue';
import * as echarts from 'echarts';
import {getByDayUsingGet} from '@/api/basicAnalysisController.ts';
import {type WeatherDataType, WeatherDataTypeLabel} from "@/constant/WeatherDataType.ts";
import dayjs from "dayjs";
import {message} from "ant-design-vue";

const props = defineProps<{
  year: dayjs.Dayjs | null;
  type: WeatherDataType;
}>();

const selectedMonth = ref<number>(dayjs().month() + 1);
const selectedDay = ref<number>(dayjs().date());
const chartRef = ref<HTMLDivElement | null>(null);
let chartInstance: echarts.ECharts | null = null;

const daysInMonth = computed(() => {
  if (!props.year || !selectedMonth.value) return 31;
  return props.year.month(selectedMonth.value - 1).daysInMonth();
});

const initChart = () => {
  if (chartRef.value) {
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
  if (!props.year || !selectedMonth.value || !selectedDay.value) {
    message.warning('请选择完整日期');
    return;
  }

  try {
    chartLoading.value = true;
    const year = props.year.format('YYYY');
    const month = selectedMonth.value.toString().padStart(2, '0');
    const day = selectedDay.value.toString().padStart(2, '0');

    const res = await getByDayUsingGet({
      day: day,
      month: month,
      type: props.type,
      year: year
    });

    if (res.data.code === 0) {
      isEmpty.value = res.data.data.length === 0;

      if (!isEmpty.value) {
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
    message.error('请求失败');
    console.error(error);
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
    // 确保图表实例存在
    initChart();
    if (!chartInstance) return;
  }

  // 处理数据格式
  const xAxisData = data.map(item => item.label);
  const seriesData = data.map(item => item.value);
  const typeName = WeatherDataTypeLabel[props.type];

  const option = {
    title: {
      text: `${props.year?.format('YYYY')}年${selectedMonth.value}月${selectedDay.value}日${typeName}数据`,
      left: 'center',
      textStyle: {
        fontSize: 16
      }
    },
    tooltip: {
      trigger: 'axis',
      formatter: (params: any) => {
        return `${params[0].axisValue}<br/>${params[0].marker} ${params[0].seriesName}: ${params[0].data}`;
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
      data: xAxisData, // 使用处理后的x轴数据
      axisLabel: {
        interval: 0,
        rotate: 30 // 如果标签太长可以旋转
      }
    },
    yAxis: {
      type: 'value',
      name: '数值'
    },
    series: [{
      name: typeName,
      type: 'line',
      data: seriesData, // 使用处理后的series数据
      itemStyle: {
        color: '#722ed1'
      },
      smooth: true,
      symbol: 'circle',
      symbolSize: 6,
      lineStyle: {
        width: 3
      },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          {offset: 0, color: 'rgba(114, 46, 209, 0.5)'},
          {offset: 1, color: 'rgba(114, 46, 209, 0.1)'}
        ])
      }
    }]
  };

  // 清除旧图表并重新设置
  chartInstance.clear();
  chartInstance.setOption(option);

  // 确保图表自适应
  setTimeout(() => {
    chartInstance?.resize();
  }, 0);
};

onMounted(() => {
  initChart();
  fetchData();
});

watch(() => props.type, () => {
  fetchData();
});

watch(() => selectedMonth.value, () => {
  selectedDay.value = 1;
});

defineExpose({
  fetchData
});
</script>

<style scoped>
#hourlyDataChart {
  width: 100%;
  background: #fff;
  padding: 16px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

#hourlyDataChart .chart-title {
  text-align: center;
  margin-bottom: 16px;
  font-size: 16px;
  color: rgba(0, 0, 0, 0.85);
}

#hourlyDataChart .input-container {
  display: flex;
  justify-content: center;
  margin-bottom: 16px;
}

#hourlyDataChart .chart {
  width: 100%;
  height: 400px;
}

#hourlyDataChart .chart-empty {
  background: #fff;
  border-radius: 4px;
  margin: 20px 0;
  padding: 20px;
}
</style>