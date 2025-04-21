<template>
  <div id="extremeAnalysisPage">
    <h1 class="page-title">极端天气分析</h1>

    <a-row :gutter="16" class="chart-row">
      <a-col :span="12">
        <StrongWindChart/>
      </a-col>
      <a-col :span="12">
        <ExtremeTempChart/>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import StrongWindChart from "@/components/extremeAnalysis/StrongWindChart.vue";
import ExtremeTempChart from "@/components/extremeAnalysis/ExtremeTempChart.vue";
</script>

<style scoped>
#extremeAnalysisPage {
  padding: 20px;
}

#extremeAnalysisPage .page-title {
  text-align: center;
  margin-bottom: 24px;
}

#extremeAnalysisPage .chart-row {
  margin-top: 16px;
}
</style>