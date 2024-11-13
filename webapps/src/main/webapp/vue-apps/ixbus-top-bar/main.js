import './initComponents.js';
import './services.js';

const appId = 'ixbusTopBar';
const lang = eXo.env.portal.language;
const url = `${eXo.env.portal.context}/${eXo.env.portal.rest}/i18n/bundle/locale.portlet.ixbus-${lang}.json`;

//getting locale ressources
export function init() {
  exoi18n.loadLanguageAsync(lang, url)
    .then(() => {
      // init Vue app when locale ressources are ready
      Vue.createApp({
        template: `<ixbus-top-bar id="${appId}" />`,
        vuetify: Vue.prototype.vuetifyOptions,
        i18n: exoi18n.i18n,
      }, `#${appId}`, 'Ixbus TopBar');
    });
}
