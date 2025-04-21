<template>
  <div id="trendAnalysisPage">
    <a-card title="趋势分析" bordered>
      <a-row>
        <a-col :span="24">
            <div class="search-container">
              <a-space>
                <div>请选择年份:</div>
                <a-date-picker
                    v-model:value="selectedYear"
                    picker="year"
                    placeholder="选择年份"
                    style="width: 200px"
                />
              </a-space>
            </div>
        </a-col>
      </a-row>
      <a-row :gutter="16">
        <a-col :span="12">
          <monthly-temperature-chart
              :year="selectedYear"
          />
        </a-col>
        <a-col :span="12">
          <monthly-wind-speed-chart
              :year="selectedYear"
          />
        </a-col>
      </a-row>
      <a-row>
        <a-col :span="24">
          <seasonal-temperature-chart
              :year="selectedYear"
          />
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import {ref} from 'vue';
import MonthlyTemperatureChart from "@/components/trendAnalysis/MonthlyTemperatureChart.vue";
import MonthlyWindSpeedChart from "@/components/trendAnalysis/MonthlyWindSpeedChart.vue";
import SeasonalTemperatureChart from "@/components/trendAnalysis/SeasonalTemperatureChart.vue";
import dayjs from "dayjs";

const selectedYear = ref(dayjs());
</script>

<style>
#trendAnalysisPage .search-container {
  margin-bottom: 20px;
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
</style>