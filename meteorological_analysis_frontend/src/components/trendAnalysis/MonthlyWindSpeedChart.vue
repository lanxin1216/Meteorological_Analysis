<template>
  <a-card title="月度平均风速趋势图" size="small" bordered>
    <a-spin :spinning="loading" tip="图表加载中...">
      <a-empty v-if="isEmpty && !loading" description="暂无数据" class="chart-empty" />
      <div v-show="!isEmpty && !loading" ref="chartRef" class="chart" />
    </a-spin>
  </a-card>
</template>

<script setup lang="ts">
import { onMounted, onBeforeUnmount, ref, watch } from 'vue';
import * as echarts from 'echarts';
import { getMonthlyWindSpeedUsingGet } from "@/api/weatherTrendController.ts";

const props = defineProps<{ year: number }>();

const chartRef = ref<HTMLElement | null>(null);
let chartInstance: echarts.ECharts | null = null;

const loading = ref(false);
const isEmpty = ref(false);

async function renderChart(year: number) {
  loading.value = true;
  const res = await getMonthlyWindSpeedUsingGet({ year: year });
  if (res.data.code === 0 && chartRef.value) {
    isEmpty.value = res.data.data?.length === 0;
    if (!isEmpty.value) {
      const xData = res.data.data.map((item) => `${item.month}月`);
      const yData = res.data.data.map((item) => item.avgSpeed);
      const option = {
        title: { text: `${year} 年月度平均风速` },
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: xData },
        yAxis: { type: 'value', name: '风速 (m/s)' },
        series: [{ type: 'line', data: yData, smooth: true, areaStyle: {} }],
      };

      if (!chartInstance) {
        chartInstance = echarts.init(chartRef.value);
      }
      chartInstance.setOption(option);
    }
  } else {
    isEmpty.value = true;
  }
  loading.value = false;
}

onMounted(() => renderChart(props.year));
watch(() => props.year, (newYear) => renderChart(newYear));
onBeforeUnmount(() => {
  chartInstance?.dispose();
});
</script>

<style scoped>
.chart {
  height: 300px;
  width: 100%;
}
.chart-empty {
  padding: 40px 0;
  text-align: center;
}
</style>