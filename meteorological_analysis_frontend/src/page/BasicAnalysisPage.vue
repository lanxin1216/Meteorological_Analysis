<template>
  <div id="basicAnalysisPage">
    <div class="search-container">
      <a-form :model="searchForm" layout="inline">
        <a-form-item label="选择年份">
          <a-date-picker
              v-model:value="searchForm.year"
              picker="year"
              placeholder="请选择年份"
              style="width: 200px"
          />
        </a-form-item>
        <a-form-item label="选择类型">
          <a-select
              v-model:value="searchForm.type"
              placeholder="请选择气象数据类型"
              style="width: 200px"
          >
            <a-select-option
                v-for="(label, type) in WeatherDataTypeLabel"
                :key="type"
                :value="type"
            >
              {{ label }}
            </a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handleSearch" :loading="loading">
            查询
          </a-button>
        </a-form-item>
      </a-form>
    </div>

    <a-alert class="info"
             message="首次加载提示：由于Hive数据仓库系统配置较低，首次查询可能需要较长时间进行分析处理，请耐心等待。后续查询会使用缓存加速。"
             show-icon
             type="warning"/>

    <div class="chart-container">
      <MonthlyAverageChart
          ref="monthlyChartRef"
          :year="searchForm.year"
          :type="searchForm.type"
      />
      <DailyAverageChart
          ref="dailyChartRef"
          :year="searchForm.year"
          :type="searchForm.type"
      />
      <HourlyDataChart
          ref="hourlyChartRef"
          :year="searchForm.year"
          :type="searchForm.type"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref, reactive} from 'vue';
import {message} from 'ant-design-vue';
import dayjs from 'dayjs';
import {
  WeatherDataType,
  WeatherDataTypeLabel,
} from '@/constant/WeatherDataType.ts';
import MonthlyAverageChart from "@/components/basicAnalysis/MonthlyAverageChart.vue";
import HourlyDataChart from "@/components/basicAnalysis/HourlyDataChart.vue";
import DailyAverageChart from "@/components/basicAnalysis/DailyAverageChart.vue";

const searchForm = reactive({
  year: dayjs(),
  type: WeatherDataType.TEMPERATURE,
});

const loading = ref(false);

const monthlyChartRef = ref<InstanceType<typeof MonthlyAverageChart> | null>(null);
const hourlyChartRef = ref<InstanceType<typeof HourlyDataChart> | null>(null);
const dailyChartRef = ref<InstanceType<typeof DailyAverageChart> | null>(null);

const handleSearch = async () => {
  if (!searchForm.year) {
    message.warning('请选择年份');
    return;
  }

  try {
    loading.value = true;
    await Promise.all([
      monthlyChartRef.value?.fetchData(),
      dailyChartRef.value?.fetchData(),
      hourlyChartRef.value?.fetchData()
    ]);
  } catch (error) {
    message.error('获取数据失败');
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
#basicAnalysisPage {
  padding: 20px;
}

#basicAnalysisPage .search-container {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

#basicAnalysisPage .info {
  margin-bottom: 16px;
}

#basicAnalysisPage .chart-container {
  display: grid;
  grid-template-columns: 1fr;
  gap: 24px;
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
</style>