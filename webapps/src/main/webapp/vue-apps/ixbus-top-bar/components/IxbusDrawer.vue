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
      <div class="d-flex overflow-hidden full-width">
        <v-tabs
          class="flex-grow-1 flex-shrink-1">
          <v-tab
            tab-value="actions"
            v-show="this.actionsCount > 0"
            href="#actions">
            {{ $t('ixbus.drawer.tab.actions') }}
            <v-avatar
              v-show="this.actionsCount > 0"
              color="secondary"
              min-height="16"
              min-width="16"
              height="auto"
              width="auto"
              class="ms-2 pa-1 aspect-ratio-1 white--text content-box-sizing">
              {{ this.actionsCount }}
            </v-avatar>
          </v-tab>
          <v-tab
            tab-value="myFolders"
            v-show="this.foldersCount > 0"
            href="#myFolders">
            {{ $t('ixbus.drawer.tab.myfolders') }}
            <v-avatar
              v-show="this.foldersCount > 0"
              color="secondary"
              min-height="16"
              min-width="16"
              height="auto"
              width="auto"
              class="ms-2 pa-1 aspect-ratio-1 white--text content-box-sizing">
              {{ this.foldersCount }}
            </v-avatar>
          </v-tab>
        </v-tabs>
      </div>
    </template>
  </exo-drawer>
</template>
<script>
export default {
  data: () => ({
    actionsCount: 0,
    foldersCount: 0
  }),
  created() {
    this.$root.$on('open-ixbus-drawer', () => {
      this.openDrawer();
    });
  },
  methods: {
    openDrawer() {
      this.$ixbusService.getCurrentUserFoldersCount()
        .then((data) => {
          this.actionsCount = data?.count || 0;
        });
      this.$refs.ixbusDrawer.open();
    },
  }
};
</script>

