<template>
  <a-card title="趋势分析" bordered>
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
    <a-row class="mt-4">
      <a-col :span="24">
        <seasonal-temperature-chart
            :year="selectedYear"
        />
      </a-col>
    </a-row>
    <a-row class="mt-4">
      <a-col>
        <a-space>
          <a-date-picker
              v-model="selectedYear"
              picker="year"
              placeholder="选择年份"
          />
        </a-space>
      </a-col>
    </a-row>
  </a-card>
</template>

<script setup lang="ts">
import {ref} from 'vue';
import MonthlyTemperatureChart from "@/components/trendAnalysis/MonthlyTemperatureChart.vue";
import MonthlyWindSpeedChart from "@/components/trendAnalysis/MonthlyWindSpeedChart.vue";
import SeasonalTemperatureChart from "@/components/trendAnalysis/SeasonalTemperatureChart.vue";

const selectedYear = ref<number>(new Date().getFullYear());
</script>