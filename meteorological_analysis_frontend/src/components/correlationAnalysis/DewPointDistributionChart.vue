<template>
  <a-card title="露点温度与温度差值的分布">
    <a-spin :spinning="loading" tip="图表加载中...">
      <a-empty v-if="isEmpty && !loading" description="暂无数据" class="chart-empty" />
      <div v-show="!isEmpty && !loading" ref="chartRef" class="chart" />
    </a-spin>
  </a-card>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from 'vue'
import * as echarts from 'echarts'
import {getTempDewPointDistributionUsingGet} from "@/api/weatherCorrelationAnalysisController.ts";

const props = defineProps<{ year: number | null }>()

const chartRef = ref<HTMLDivElement | null>(null)
let chart: echarts.ECharts

const loading = ref(false)
const isEmpty = ref(false)

const fetchData = async () => {
  if (!props.year) return
  loading.value = true
  const res = await getTempDewPointDistributionUsingGet({year: props.year})
  if (res.data.code === 0) {
    isEmpty.value = res.data.data?.length === 0
    renderChart(res.data.data)
  } else {
    isEmpty.value = true
  }
  loading.value = false
}

const renderChart = (data: any[]) => {
  const labels = data.map(item => `${item.tempDiffRange}℃`)
  const values = data.map(item => item.recordCount)

  chart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: labels },
    yAxis: { type: 'value', name: '记录数' },
    series: [{ type: 'bar', data: values, itemStyle: { color: '#52c41a' } }]
  })
}

onMounted(() => {
  if (chartRef.value) chart = echarts.init(chartRef.value)
  if (props.year) fetchData()
})

watch(() => props.year, () => {
  fetchData()
})
</script>

<style scoped>
.chart {
  height: 400px;
  width: 100%;
}
.chart-empty {
  padding: 40px 0;
  text-align: center;
}
</style>
