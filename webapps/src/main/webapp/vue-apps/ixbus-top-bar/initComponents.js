import IxbusTopBar from './components/IxbusTopBar.vue';
import IxbusTopBarButton from './components/IxbusTopBarButton.vue';
import IxbusDrawer from './components/IxbusDrawer.vue';
import IxbusDocument from './components/IxbusDocument.vue';

const components = {
  'ixbus-top-bar': IxbusTopBar,
  'ixbus-top-bar-button': IxbusTopBarButton,
  'ixbus-drawer': IxbusDrawer,
  'ixbus-document': IxbusDocument,
};

for (const key in components) {
  Vue.component(key, components[key]);
}
