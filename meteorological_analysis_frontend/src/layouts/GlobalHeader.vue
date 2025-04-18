<template>
  <div id="globalHeader">
    <a-row :wrap="false">
      <a-col flex="400px">
        <RouterLink to="/">
          <div class="title-bar">
            <div class="title">哈尔滨市&气象历史数据分析平台</div>
          </div>
        </RouterLink>
      </a-col>
      <a-col flex="auto">
        <a-menu
            v-model:selectedKeys="current"
            :items="items"
            mode="horizontal"
            @click="doMenuClick"
        />
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
import {h, ref} from 'vue'
import {HomeOutlined} from '@ant-design/icons-vue'
import {type MenuProps} from 'ant-design-vue'
import {useRouter} from 'vue-router'

const current = ref<string[]>(['home'])
const items = ref<MenuProps['items']>([
  {
    key: '/',
    icon: () => h(HomeOutlined),
    label: '首页',
    title: '首页',
  },
  {
    key: '/uploadData',
    label: '上传数据',
    title: '上传数据',
  },
  {
    key: '/about',
    label: '关于',
    title: '关于',
  }
])

const router = useRouter();
// 监听路由变化，更新当前选中菜单
router.afterEach((to, from, next) => {
  current.value = [to.path];
});

// 路由跳转事件
const doMenuClick = ({key}: { key: string }) => {
  router.push({
    path: key,
  });
};

</script>

<style scoped>
.title-bar {
  display: flex;
  align-items: center;
}

.title {
  color: black;
  font-size: 18px;
  margin-left: 16px;
}
</style>
