<template>
  <a-card title="季节温度对比图" size="small" bordered>
    <a-spin :spinning="loading" tip="图表加载中...">
      <a-empty v-if="isEmpty && !loading" description="暂无数据" class="chart-empty" />
      <div v-show="!isEmpty && !loading" ref="chartRef" class="chart" />
    </a-spin>
  </a-card>
</template>

<script setup lang="ts">
import { onMounted, onBeforeUnmount, ref, watch } from 'vue';
import * as echarts from 'echarts';
import { getSeasonalTemperatureUsingGet } from "@/api/weatherTrendController.ts";

const props = defineProps<{ year: number }>();

const chartRef = ref<HTMLElement | null>(null);
let chartInstance: echarts.ECharts | null = null;

const loading = ref(false);
const isEmpty = ref(false);

async function renderChart(year: number) {
  loading.value = true;
  const res = await getSeasonalTemperatureUsingGet({ year: year });
  if (res.data.code === 0 && chartRef.value) {
    isEmpty.value = res.data.data?.length === 0;
    if (!isEmpty.value) {
      const seasons = ['春', '夏', '秋', '冬'];
      const yData = seasons.map((season) => {
        const item = res.data.data.find((d) => d.season === season);
        return item?.avgTemp ?? 0;
      });
      const option = {
        title: { text: `${year} 年四季平均温度对比图` },
        tooltip: { trigger: 'axis' },
        xAxis: { type: 'category', data: seasons },
        yAxis: { type: 'value', name: '温度 (°C)' },
        series: [{ type: 'bar', data: yData, itemStyle: { color: '#73C0DE' } }],
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