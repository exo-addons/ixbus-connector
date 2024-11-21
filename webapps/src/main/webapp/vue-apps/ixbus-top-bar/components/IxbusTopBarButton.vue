<template>
  <v-btn
    :title="$t('IxbusPortlet.label.iconTooltip')"
    :aria-label="$t('IxbusPortlet.label.iconTooltip')"
    class="ixbusTopBarBadge"
    id="ixBusTopBarButton"
    icon
    @click="$root.$emit('open-ixbus-drawer')">
    <v-badge
      :value="count > 0"
      :content="count"
      flat
      top
      right
      color="var(--allPagesBadgePrimaryColor, #d32a2a)"
      overlap>
      <v-icon size="22" class="icon-default-color">fa-file-contract</v-icon>
    </v-badge>
  </v-btn>
</template>
<script>
export default {
  data: () => ({
    count: 0
  }),
  created() {
    this.$ixbusService.getCurrentUserActionsCount()
      .then((data) => {
        this.count = data?.count || 0;
      });
  }
};
</script>
