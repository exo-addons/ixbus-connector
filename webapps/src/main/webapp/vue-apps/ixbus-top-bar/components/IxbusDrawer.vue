<template>
  <exo-drawer
    ref="ixbusDrawer"
    class="ixbusDrawer"
    right
    allow-expand>
    <template slot="title">
      {{ $t('IxbusPortlet.drawer.title') }}
    </template>
    <template slot="content">

    </template>
  </exo-drawer>
</template>
<script>
export default {
  created() {
    this.$root.$on('open-ixbus-drawer', () => {
      this.openDrawer();
    });
  },
  methods: {
    openDrawer() {
      this.$refs.ixbusDrawer.open();
    },
  }
};
</script>

