<template>
  <div id="dewPointDistributionChart">
    <a-card title="露点温度与温度差值的分布">
      <div class="card-content">
        <a-spin :spinning="loading" tip="图表加载中...">
          <a-empty v-if="isEmpty && !loading" description="暂无数据" class="chart-empty"/>
          <div v-show="!isEmpty && !loading" ref="chartRef" class="chart"/>
        </a-spin>
      </div>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import {ref, watch, onMounted} from 'vue'
import * as echarts from 'echarts'
import {getTempDewPointDistributionUsingGet} from "@/api/weatherCorrelationAnalysisController.ts";
import dayjs from "dayjs";
import {mockTempDewPointData} from "@/mock/mockTempDewPointData.ts";

const props = defineProps<{ year: dayjs.Dayjs }>()

const chartRef = ref<HTMLDivElement | null>(null)
let chart: echarts.ECharts

const loading = ref(false)
const isEmpty = ref(false)

const fetchData = async () => {
  if (!props.year) return
  loading.value = true
  const newYear = props.year.format('YYYY')
  // todo API调用
  // const res = await getTempDewPointDistributionUsingGet({year: newYear})
  // 开发测试时使用模拟数据
  const res = { data: mockTempDewPointData }
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
    tooltip: {trigger: 'axis'},
    xAxis: {type: 'category', data: labels},
    yAxis: {type: 'value', name: '记录数'},
    series: [{type: 'bar', data: values, itemStyle: {color: '#52c41a'}}]
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
#dewPointDistributionChart .card-content  {
  min-height: 300px;
}

#dewPointDistributionChart .chart {
  height: 400px;
  width: 100%;
}

#dewPointDistributionChart .chart-empty {
  padding: 40px 0;
  text-align: center;
}
</style>
