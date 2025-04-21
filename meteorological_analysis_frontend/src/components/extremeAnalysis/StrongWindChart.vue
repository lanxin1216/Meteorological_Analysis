<template>
  <div class="chart-card">
    <h3 class="chart-title">强风天气统计（风速 ≥ {{ threshold }}m/s）</h3>
    <a-form layout="inline" class="chart-controls">
      <a-form-item label="风速阈值(m/s)">
        <a-input-number
            v-model:value="threshold"
            :min="5"
            :max="20"
            :step="1"
            @change="fetchData"
        />
      </a-form-item>
    </a-form>

    <a-spin :spinning="loading" tip="数据加载中...">
      <div v-if="data.length > 0" ref="chartRef" class="chart-container"></div>
      <a-empty v-else description="暂无数据"/>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import {ref, onMounted, watch} from 'vue';
import * as echarts from 'echarts';
import {getStrongWindDaysUsingGet} from '@/api/weatherAnalysisExtremeController.ts';
import {message} from 'ant-design-vue';

const emit = defineEmits(['update:threshold']);

const threshold = ref(10);
const data = ref<any[]>([]);
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
  try {
    loading.value = true;
    const res = await getStrongWindDaysUsingGet({threshold: threshold.value});
    if (res.data.code === 0) {
      data.value = res.data.data;
      updateChart();
    } else {
      message.error(res.data.message || '获取强风数据失败');
    }
  } catch (error) {
    console.error('获取强风数据失败:', error);
    message.error('获取强风数据失败');
  } finally {
    loading.value = false;
  }
};

const updateChart = () => {
  if (!chartInstance || !data.value.length) return;

  const option = {
    title: {
      text: `各年风速 ≥ ${threshold.value}m/s 天数统计`,
      left: 'center',
      textStyle: {
        fontSize: 14
      }
    },
    tooltip: {
      trigger: 'axis',
      formatter: (params: any) => {
        return `${params[0].axisValue}年<br/>强风天数: ${params[0].data}天`;
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
      data: data.value.map(item => item.year),
      axisLabel: {
        interval: 0
      }
    },
    yAxis: {
      type: 'value',
      name: '天数',
      minInterval: 1
    },
    series: [{
      name: '强风天数',
      type: 'bar',
      data: data.value.map(item => item.days),
      itemStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          {offset: 0, color: '#ff7b7b'},
          {offset: 1, color: '#ff4e4e'}
        ]),
        borderRadius: [4, 4, 0, 0]
      },
      barWidth: '60%',
      label: {
        show: true,
        position: 'top'
      },
      markLine: {
        data: [{
          type: 'average',
          name: '平均值'
        }]
      }
    }]
  };

  chartInstance.setOption(option);
};

watch(threshold, (newVal) => {
  emit('update:threshold', newVal);
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

.chart-controls {
  margin-bottom: 16px;
  justify-content: center;
}
</style>