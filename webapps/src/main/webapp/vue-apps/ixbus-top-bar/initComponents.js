import IxbusTopBar from './components/IxbusTopBar.vue';
import IxbusTopBarButton from './components/IxbusTopBarButton.vue';

const components = {
  'ixbus-top-bar': IxbusTopBar,
  'ixbus-top-bar-button': IxbusTopBarButton,
};

for (const key in components) {
  Vue.component(key, components[key]);
}
