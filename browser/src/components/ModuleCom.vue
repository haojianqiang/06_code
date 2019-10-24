<template>
  <div class="module-menu" v-if="modules!=null">
    <ul>
      <li v-on:click="setCurModule($event)" v-for="item in modules" :key="item.moduleId" :data-id="item.moduleId">
        <a href="javascript:;">{{item.moduleName}}</a>
      </li>
    </ul>
  </div>
</template>
<script>
export default {
  name: "Module",
  data() {
    return {
      modules:
        this.$store.state.modules == null ? null : this.$store.state.modules
    };
  },
  methods: {
      setCurModule(e){
          // 获取选中Module的ID
          let id = e.currentTarget.getAttribute("data-id");
          this.$store.dispatch('setCurModule',id);
      }
  }
};
</script>
<style scoped>
.module-menu {
  width: 100%;
  display: flex;
  flex-direction: column;
  flex: 1;
}
.module-menu ul {
  display: flex;
}
.module-menu li {
  flex: 1;
  background: #5fb1ff;
}
.module-menu li a {
  display: block;
  font-size: 22px;
  background: #2f8dd3;
  color: #fff;
  text-decoration: none;
  text-align: center;
}
</style>


