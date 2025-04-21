<template>
  <div id="seasonalTemperatureChart">
    <a-card title="季节温度对比图" size="small" bordered>
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
import {onMounted, onBeforeUnmount, ref, watch} from 'vue';
import * as echarts from 'echarts';
import {getSeasonalTemperatureUsingGet} from "@/api/weatherTrendController.ts";
import dayjs from "dayjs";

const props = defineProps<{ year: dayjs.Dayjs }>();

const chartRef = ref<HTMLElement | null>(null);
let chartInstance: echarts.ECharts | null = null;

const loading = ref(false);
const isEmpty = ref(false);

async function renderChart(year: dayjs.Dayjs) {
  const newYear = props.year.format('YYYY')
  loading.value = true;
  const res = await getSeasonalTemperatureUsingGet({year: newYear});
  if (res.data.code === 0 && chartRef.value) {
    isEmpty.value = res.data.data?.length === 0;
    if (!isEmpty.value) {
      const seasons = ['春', '夏', '秋', '冬'];
      const yData = seasons.map((season) => {
        const item = res.data.data.find((d) => d.season === season);
        return item?.avgTemp ?? 0;
      });
      const option = {
        title: {text: `${year} 年四季平均温度对比图`},
        tooltip: {trigger: 'axis'},
        xAxis: {type: 'category', data: seasons},
        yAxis: {type: 'value', name: '温度 (°C)'},
        series: [{type: 'bar', data: yData, itemStyle: {color: '#73C0DE'}}],
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

onMounted(() => {
  renderChart(props.year)
});
watch(() => props.year, (newYear) => renderChart(newYear));
onBeforeUnmount(() => {
  chartInstance?.dispose();
});
</script>

<style scoped>
#seasonalTemperatureChart {
  margin-top: 20px;
}

#seasonalTemperatureChart .card-content {
  min-height: 300px;
}

#seasonalTemperatureChart .chart {
  height: 300px;
  width: 100%;
}

#seasonalTemperatureChart .chart-empty {
  padding: 40px 0;
  text-align: center;
}
</style>