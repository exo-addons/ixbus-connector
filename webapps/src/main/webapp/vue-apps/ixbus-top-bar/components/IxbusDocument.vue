<template>
  <v-card
    flat
    outlined
    hover class="pa-2 mb-2">
      <div class="d-flex flex-wrap">
        <div class="flex-grow-1 font-weight-bold text-truncate-2" style="flex-basis:0">
          {{ this.document?.name }}
        </div>
        <div class="flex-grow-1 text-right" style="flex-basis:0">
          <v-chip color="primary" style="text-transform:capitalize" v-if="this.document?.action">
            {{ this.document?.action }}
          </v-chip>
        </div>
      </div>
      <div class="d-flex flex-wrap">
        <div class="flex-grow-1 text-subtitle" style="flex-basis:0">
          {{ this.formatedCreationDate }}
        </div>
        <div class="flex-grow-1 text-right text-subtitle" style="flex-basis:0" v-sanitized-html="this.formatedDueDate">
        </div>
      </div>
      <div class="d-flex flex-wrap">
        <div class="flex-grow-1 text-subtitle" style="flex-basis:0">
          {{ $t('ixbus.drawer.referent') }} : <span class="font-weight-bold">{{ this.document?.referentFirstName }} {{ this.document?.referentLastName }}</span>
        </div>
        <div class="flex-grow-1 text-right text-subtitle" style="flex-basis:0">
          {{ $t('ixbus.drawer.nature') }} : <span class="font-weight-bold">{{ this.document?.nature }}</span>
        </div>
      </div>
  </v-card>
</template>

<script>
export default {
  props: {
    document : {
      type: Object,
      default: null,
    },
  },
  computed: {
    formatedCreationDate() {
      if (this.document?.creationDate) {
        const date = new Date(this.document.creationDate);
        return `${this.$t('ixbus.drawer.createdThe')} ${date.toLocaleString().substring(0,date.toLocaleString().length-3)}`;
      } else {
        return '';
      }
    },
    formatedDueDate() {
      if (this.document?.dueDate) {
        const date = new Date(this.document.dueDate);
        return `${this.$t('ixbus.drawer.dueDate')} : <span class="error-color font-weight-bold">${date.toLocaleDateString()}</span>`;
      } else {
        return '';
      }
    }
  }
}
</script>
