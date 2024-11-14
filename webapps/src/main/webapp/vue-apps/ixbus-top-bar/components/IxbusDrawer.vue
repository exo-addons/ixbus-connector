<template>
  <exo-drawer
    ref="ixbusDrawer"
    class="ixbusDrawer"
    right
    allow-expand>
    <template slot="title">
      {{ $t('IxbusPortlet.drawer.title') }}
    </template>
    <template #titleIcons>
      <div class="full-height d-flex align-center">
        <v-btn
          :title="$t('ixbus.drawer.createFolder')"
          color="primary"
          elevation="0"
          small
          @click="createFolder">
          <v-icon
            color="while"
            class="me-2"
            size="18">
            fa-plus
          </v-icon>
          {{ $t('ixbus.drawer.createFolder') }}
        </v-btn>
      </div>
    </template>
    <template slot="content">
      <div class="d-flex overflow-hidden full-width">
        <v-tabs
          class="flex-grow-1 flex-shrink-1"
          v-model="tab">
          <v-tab
            value="actions"
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
            value="folders"
            v-show="this.foldersCount > 0"
            href="#folders">
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
      <v-tabs-items
        v-model="tab">
        <v-tab-item value="actions">
          <v-list v-if="actions?.length" class="ma-4">
            <ixbus-document v-for="d in actions"
              :key="d.id"
              :document="d"/>
          </v-list>
        </v-tab-item>
        <v-tab-item value="folders" class="ma-4">
          <v-list v-if="folders?.length">
            <ixbus-document v-for="d in folders"
              :key="d.id"
              :document="d"/>
          </v-list>
        </v-tab-item>
      </v-tabs-items>
    </template>
  </exo-drawer>
</template>
<script>
export default {
  data: () => ({
    actionsCount: 0,
    foldersCount: 0,
    createUrl: '',
    folders: null,
    actions:null,
    tab: 'actions',
  }),
  created() {
    this.$root.$on('open-ixbus-drawer', () => {
      this.openDrawer();
    });
  },
  methods: {
    openDrawer() {
      this.$ixbusService.getCurrentUserActionsCount()
        .then((data) => {
          this.actionsCount = data?.count || 0;
        });

      this.$ixbusService.getCurrentUserActions()
        .then((data) => {
          this.actions = data;
        });

      this.$ixbusService.getCurrentUserFoldersCount()
        .then((data) => {
          this.foldersCount = data?.count || 0;
        });

      this.$ixbusService.getCurrentUserFolders()
        .then((data) => {
          this.folders = data;
        });

      this.$ixbusService.getSettings()
        .then((settings) => {
          this.createUrl = settings?.createUrl || '';
        });


      this.$refs.ixbusDrawer.open();
    },
    createFolder() {
      window.open(this.createUrl,'_blank');
    }
  }
};
</script>

