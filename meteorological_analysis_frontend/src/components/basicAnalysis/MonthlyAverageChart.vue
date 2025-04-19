<template>
  <div id="monthlyAverageChart">
    <h2 class="chart-title">{{ props.year?.format('YYYY') }}年每月平均数据</h2>
    <a-spin :spinning="chartLoading" tip="图表数据加载中...">
      <a-empty
          v-if="isEmpty && !chartLoading"
          description="暂无数据"
          class="chart-empty"
      />
      <div v-else ref="chartRef" class="chart"></div>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import {ref, onMounted, watch, nextTick} from 'vue';
import * as echarts from 'echarts';
import { getByYearUsingGet } from '@/api/basicAnalysisController.ts';
import type {WeatherDataType} from "@/constant/WeatherDataType.ts";
import dayjs from "dayjs";
import {message} from "ant-design-vue";

const props = defineProps<{
  year: dayjs.Dayjs | null;
  type: WeatherDataType;
}>();

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

const fetchData = async () => {
  if (!props.year) {
    message.warning('请选择年份');
    return;
  }

  try {
    chartLoading.value = true;
    const year = props.year.format('YYYY');
    const res = await getByYearUsingGet({
      year,
      type: props.type
    });

    if (res.data.code === 0) {
      isEmpty.value = res.data.data.length === 0;
      if (!isEmpty.value) {
        await nextTick();
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

  // 确保数据按月份顺序排列
  const sortedData = [...data].sort((a, b) => {
    const monthA = parseInt(a.label.replace('月', ''));
    const monthB = parseInt(b.label.replace('月', ''));
    return monthA - monthB;
  });

  const unit = getUnitByType(props.type);
  const seriesName = `每月平均${unit}`;

  const option = {
    title: {
      text: `${props.year?.format('YYYY')}年每月平均数据`,
      left: 'center',
      textStyle: {
        fontSize: 16,
        fontWeight: 'bold'
      },
      subtext: `数据类型: ${props.type}`,
      subtextStyle: {
        fontSize: 12,
        color: '#666'
      }
    },
    tooltip: {
      trigger: 'axis',
      formatter: (params: any) => {
        return `${params[0].axisValue}<br/>${params[0].marker} ${seriesName}: ${params[0].data}${unit}`;
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
        interval: 0
      },
      axisLine: {
        lineStyle: {
          color: '#666'
        }
      }
    },
    yAxis: {
      type: 'value',
      name: unit,
      axisLabel: {
        formatter: `{value} ${unit}`
      },
      axisLine: {
        lineStyle: {
          color: '#666'
        }
      },
      splitLine: {
        lineStyle: {
          type: 'dashed'
        }
      }
    },
    series: [{
      name: seriesName,
      type: 'bar',
      data: sortedData.map(item => item.value),
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: '#1890ff' },
          { offset: 1, color: '#36cbcb' }
        ]),
        borderRadius: [4, 4, 0, 0]
      },
      barWidth: '60%',
      label: {
        show: true,
        position: 'top',
        formatter: (params: any) => {
          return `${params.value}${unit}`;
        }
      },
      markLine: {
        data: [
          { type: 'average', name: '平均值' }
        ],
        label: {
          formatter: '平均值: {c}' + unit
        }
      }
    }],
    dataZoom: [{
      type: 'inside',
      start: 0,
      end: 100
    }, {
      start: 0,
      end: 100
    }]
  };

  chartInstance.clear();
  chartInstance.setOption(option);
  setTimeout(() => {
    chartInstance?.resize();
  }, 0);
};

onMounted(() => {
  initChart();
  fetchData();
});

watch(() => [props.year, props.type], () => {
  fetchData();
}, { immediate: true });

defineExpose({
  fetchData
});
</script>

<style scoped>
#monthlyAverageChart {
  width: 100%;
  background: #fff;
  padding: 16px;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

.chart-title {
  text-align: center;
  margin-bottom: 16px;
  font-size: 18px;
  font-weight: 500;
  color: rgba(0, 0, 0, 0.85);
}

.chart {
  width: 100%;
  height: 450px;
  min-height: 450px;
}

.chart-empty {
  padding: 40px 0;
  text-align: center;
  background: #fff;
  border-radius: 4px;
}
</style>