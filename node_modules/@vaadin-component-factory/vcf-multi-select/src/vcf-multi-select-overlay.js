/*
 * @license	 * Copyright 2000-2020 Vaadin Ltd.
 * Copyright (C) 2015 Vaadin Ltd.	 *
 * This program is available under Commercial Vaadin Add-On License 3.0 (CVALv3).	 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * See the file LICENSE.md distributed with this software for more information about licensing.	 * use this file except in compliance with the License. You may obtain a copy of
 * See [the website]{@link https://vaadin.com/license/cval-3} for the complete license.	 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
import '@polymer/polymer/lib/elements/dom-module.js';

import { OverlayElement } from '@vaadin/vaadin-overlay/src/vaadin-overlay.js';
const $_documentContainer = document.createElement('template');

$_documentContainer.innerHTML = `<dom-module id="vcf-multi-select-overlay-styles" theme-for="vcf-multi-select-overlay">
  <template>
    <style>
      :host {
        align-items: flex-start;
        justify-content: flex-start;
      }
    </style>
  </template>
</dom-module>`;

document.head.appendChild($_documentContainer.content);
/**
  * The overlay element.
  *
  * ### Styling
  *
  * See [`<vaadin-overlay>` documentation](https://github.com/vaadin/vaadin-overlay/blob/master/src/vaadin-overlay.html)
  * for `<vcf-multi-select-overlay>` parts.
  *
  * See [ThemableMixin – how to apply styles for shadow parts](https://github.com/vaadin/vaadin-themable-mixin/wiki)
  *
  * @extends PolymerElement
  */
class VcfMultiSelectOverlayElement extends OverlayElement {
  static get is() {
    return 'vcf-multi-select-overlay';
  }
}
customElements.define(VcfMultiSelectOverlayElement.is, VcfMultiSelectOverlayElement);
