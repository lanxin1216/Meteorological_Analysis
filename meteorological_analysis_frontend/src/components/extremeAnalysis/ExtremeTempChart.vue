<template>
  <div class="chart-card">
    <h3 class="chart-title">年度极端温度数据</h3>
    <a-form layout="inline" class="chart-controls">
      <a-form-item label="选择年份">
        <a-date-picker
            v-model:value="year"
            picker="year"
            placeholder="请选择年份"
            style="width: 200px"
            @change="fetchData"
        />
      </a-form-item>
    </a-form>

    <a-spin :spinning="loading" tip="数据加载中...">
      <div v-if="data" class="temp-data-container">
        <a-descriptions bordered>
          <a-descriptions-item label="年份">{{ data.year }}</a-descriptions-item>
          <a-descriptions-item label="最高温度">
            {{ formatTemp(data.maxTemperature) }}°C
            <span class="time-text">({{ formatDateTime(data.maxTemperatureTime) }})</span>
          </a-descriptions-item>
          <a-descriptions-item label="最低温度">
            {{ formatTemp(data.minTemperature) }}°C
            <span class="time-text">({{ formatDateTime(data.minTemperatureTime) }})</span>
          </a-descriptions-item>
        </a-descriptions>
        <div ref="chartRef" class="chart-container"></div>
      </div>
      <a-empty v-else description="请选择年份查看极端温度数据"/>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue';
import * as echarts from 'echarts';
import { getExtremeTemperaturesUsingGet } from '@/api/weatherAnalysisExtremeController.ts';
import { message } from 'ant-design-vue';
import dayjs from 'dayjs';

const emit = defineEmits(['update:year']);

const year = ref(dayjs());
const data = ref<any>(null);
const loading = ref(false);
const chartRef = ref<HTMLElement | null>(null);
let chartInstance: echarts.ECharts | null = null;

const initChart = () => {
  if (chartRef.value) {
    chartInstance = echarts.init(chartRef.value);
    window.addEventListener('resize', resizeChart);
  }
};

const resizeChart = () => {
  chartInstance?.resize();
};

const fetchData = async () => {
  if (!year.value) return;

  try {
    loading.value = true;
    const yearValue = year.value.year();
    const res = await getExtremeTemperaturesUsingGet({ year: yearValue });
    if (res.data.code === 0) {
      data.value = res.data.data;
      updateChart();
    } else {
      message.error(res.data.message || '获取极端温度数据失败');
    }
  } catch (error) {
    console.error('获取极端温度数据失败:', error);
    message.error('获取极端温度数据失败');
  } finally {
    loading.value = false;
  }
};

const updateChart = () => {
  if (!chartInstance || !data.value) return;

  const option = {
    title: {
      text: `${data.value.year}年极端温度`,
      left: 'center',
      textStyle: {
        fontSize: 14
      }
    },
    tooltip: {
      trigger: 'axis',
      formatter: (params: any) => {
        return `${params[0].seriesName}: ${params[0].data}°C`;
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
      data: ['最高温度', '最低温度'],
      axisLabel: {
        interval: 0
      }
    },
    yAxis: {
      type: 'value',
      name: '温度(°C)'
    },
    series: [{
      name: '极端温度',
      type: 'bar',
      data: [
        formatTemp(data.value.maxTemperature),
        formatTemp(data.value.minTemperature)
      ],
      itemStyle: {
        color: (params: any) => {
          return params.dataIndex === 0
              ? new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#ff9a9a' },
                { offset: 1, color: '#ff4e4e' }
              ])
              : new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#9ac2ff' },
                { offset: 1, color: '#4e8cff' }
              ]);
        },
        borderRadius: [4, 4, 0, 0]
      },
      barWidth: '40%',
      label: {
        show: true,
        position: 'top',
        formatter: '{c}°C'
      }
    }]
  };

  chartInstance.setOption(option);
};

const formatTemp = (temp: any) => {
  if (typeof temp === 'object' && temp !== null) {
    return Object.values(temp)[0]?.toFixed(1) || 'N/A';
  }
  return Number(temp).toFixed(1);
};

const formatDateTime = (time: string) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm');
};

watch(year, (newVal) => {
  emit('update:year', newVal);
});

onMounted(() => {
  initChart();
  fetchData();
});

defineExpose({
  fetchData
});
</script>

<style scoped>
.chart-card {
  padding: 16px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  height: 100%;
}

.chart-title {
  text-align: center;
  margin-bottom: 16px;
}

.chart-container {
  width: 100%;
  height: 400px;
}

.temp-data-container {
  margin-bottom: 20px;
}

.time-text {
  color: #888;
  font-size: 12px;
  margin-left: 8px;
}

.ant-descriptions-item-label {
  font-weight: bold;
}

.chart-controls {
  margin-bottom: 16px;
  justify-content: center;
}
</style>