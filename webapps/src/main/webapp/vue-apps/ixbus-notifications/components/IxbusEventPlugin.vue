<template>
  <user-notification-template
    :notification="notification"
    :message="message"
    :loading="loading"
    url="javascript:if(document.getElementById('ixBusTopBarButton')) document.getElementById('ixBusTopBarButton').click();">
    <template #avatar>
      <div>
        <v-icon size="36">fa-file-contract</v-icon>
      </div>
    </template>
    <template #actions>
      <div class="text-truncate">
        {{ subMessage }}
      </div>
    </template>
  </user-notification-template>
</template>
<script>
export default {
  data: () => ({
    space: null,
  }),
  props: {
    notification: {
      type: Object,
      default: null,
    },
  },
  computed: {
    message() {
      return this.$t('ixbus.notification.received.message', {
        0: `<strong>${this.notification?.parameters?.documentName}</strong>`,
        1: `<span class="text-lowercase">${this.notification?.parameters?.status}</span>`,
      });
    },
    subMessage() {
      return `${this.$t('ixbus.notification.received.subMessage')} : ${this.notification?.parameters?.nature}`;
    }
  }
};
</script>
