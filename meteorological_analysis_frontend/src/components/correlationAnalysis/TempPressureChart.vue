<template>
  <div id="tempPressureChart">
    <a-card title="温度与气压的月度相关性">
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
import {getTempPressureCorrelationUsingGet} from "@/api/weatherCorrelationAnalysisController.ts";
import dayjs from "dayjs";
import {mockTempPressureCorrelation} from "@/mock/mockTempPressureCorrelation.ts";

const props = defineProps<{ year: dayjs.Dayjs  }>()

const chartRef = ref<HTMLDivElement | null>(null)
let chart: echarts.ECharts

const loading = ref(false)
const isEmpty = ref(false)

const fetchData = async () => {
  if (!props.year) return
  loading.value = true
  const newYear = props.year.format('YYYY')
  const res = await getTempPressureCorrelationUsingGet({year: newYear})
  // todo mock数据
  // const res = { data: mockTempPressureCorrelation }
  if (res.data.code === 0) {
    isEmpty.value = res.data.data?.length === 0
    renderChart(res.data.data)
  } else {
    isEmpty.value = true
  }
  loading.value = false
}

const renderChart = (data: any[]) => {
  const months = data.map(item => `${item.month}月`)
  const values = data.map(item => item.correlationValue?.value || 0)

  chart.setOption({
    tooltip: {trigger: 'axis'},
    xAxis: {type: 'category', data: months},
    yAxis: {type: 'value', name: '相关系数'},
    series: [{type: 'bar', data: values, itemStyle: {color: '#1890ff'}}]
  })
}

onMounted(() => {
  if (chartRef.value) chart = echarts.init(chartRef.value)
  if (props.year) {
    fetchData()
  }
})

watch(() => props.year, () => {
  fetchData()
})
</script>

<style scoped>
#tempPressureChart .card-content  {
  min-height: 300px;
}

#tempPressureChart .chart {
  height: 400px;
  width: 100%;
}

#tempPressureChart .chart-empty {
  padding: 40px 0;
  text-align: center;
}
</style>
