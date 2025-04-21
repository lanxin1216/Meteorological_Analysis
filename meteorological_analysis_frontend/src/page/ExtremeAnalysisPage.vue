<template>
  <div id="extremeAnalysisPage">
    <h1 class="page-title">极端天气分析</h1>

    <a-row :gutter="16" class="chart-row">
      <a-col :span="12">
        <StrongWindChart v-model:threshold="windThreshold"/>
      </a-col>
      <a-col :span="12">
        <ExtremeTempChart v-model:year="selectedYear"/>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import {ref} from 'vue';
import dayjs from "dayjs";
import StrongWindChart from "@/components/extremeAnalysis/StrongWindChart.vue";
import ExtremeTempChart from "@/components/extremeAnalysis/ExtremeTempChart.vue";


const windThreshold = ref(10);
const selectedYear = ref(dayjs());
</script>

<style scoped>
#extremeAnalysisPage {
  padding: 20px;
}

.page-title {
  text-align: center;
  margin-bottom: 24px;
}

.chart-row {
  margin-top: 16px;
}
</style>