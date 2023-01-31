/*
 * Copyright 2000-2020 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
import { PolymerElement } from '@polymer/polymer/polymer-element.js';

import '@polymer/polymer/lib/elements/custom-style.js';
import { ThemableMixin } from '@vaadin/vaadin-themable-mixin/vaadin-themable-mixin.js';
import '@vaadin/vaadin-themable-mixin/vaadin-theme-property-mixin.js';
import { ControlStateMixin } from '@vaadin/vaadin-control-state-mixin/vaadin-control-state-mixin.js';
import { IronResizableBehavior } from '@polymer/iron-resizable-behavior/iron-resizable-behavior.js';
import '@polymer/iron-media-query/iron-media-query.js';
import { ElementMixin } from '@vaadin/vaadin-element-mixin/vaadin-element-mixin.js';
import './vcf-multi-select-overlay.js';
import './vcf-multi-select-text-field.js';
import '../theme/lumo/vcf-multi-select.js';
import { html } from '@polymer/polymer/lib/utils/html-tag.js';
import { FlattenedNodesObserver } from '@polymer/polymer/lib/utils/flattened-nodes-observer.js';
import { mixinBehaviors } from '@polymer/polymer/lib/legacy/class.js';
const $_documentContainer = document.createElement('template');

$_documentContainer.innerHTML = `<custom-style>
  <style>
    @font-face {
      font-family: "vcf-multi-select-icons";
      src: url(data:application/font-woff;charset=utf-8;base64,d09GRgABAAAAAASEAAsAAAAABDgAAQAAAAAAAAAAAAAAAAAAAAAAAAAAAABPUy8yAAABCAAAAGAAAABgDxIGKmNtYXAAAAFoAAAAVAAAAFQXVtKHZ2FzcAAAAbwAAAAIAAAACAAAABBnbHlmAAABxAAAAHwAAAB8CohkJ2hlYWQAAAJAAAAANgAAADYOavgEaGhlYQAAAngAAAAkAAAAJAarA8ZobXR4AAACnAAAABQAAAAUCAABP2xvY2EAAAKwAAAADAAAAAwAKABSbWF4cAAAArwAAAAgAAAAIAAHABduYW1lAAAC3AAAAYYAAAGGmUoJ+3Bvc3QAAARkAAAAIAAAACAAAwAAAAMEAAGQAAUAAAKZAswAAACPApkCzAAAAesAMwEJAAAAAAAAAAAAAAAAAAAAARAAAAAAAAAAAAAAAAAAAAAAQAAA6QADwP/AAEADwABAAAAAAQAAAAAAAAAAAAAAIAAAAAAAAwAAAAMAAAAcAAEAAwAAABwAAwABAAAAHAAEADgAAAAKAAgAAgACAAEAIOkA//3//wAAAAAAIOkA//3//wAB/+MXBAADAAEAAAAAAAAAAAAAAAEAAf//AA8AAQAAAAAAAAAAAAIAADc5AQAAAAABAAAAAAAAAAAAAgAANzkBAAAAAAEAAAAAAAAAAAACAAA3OQEAAAAAAQE/AUAC6QIVABQAAAEwFx4BFxYxMDc+ATc2MTAjKgEjIgE/ISJPIiEhIk8iIUNCoEJDAhUhIk8iISEiTyIhAAEAAAABAABvL5bdXw889QALBAAAAAAA1jHaeQAAAADWMdp5AAAAAALpAhUAAAAIAAIAAAAAAAAAAQAAA8D/wAAABAAAAAAAAukAAQAAAAAAAAAAAAAAAAAAAAUEAAAAAAAAAAAAAAAAAAAABAABPwAAAAAACgAUAB4APgABAAAABQAVAAEAAAAAAAIAAAAAAAAAAAAAAAAAAAAAAAAADgCuAAEAAAAAAAEABwAAAAEAAAAAAAIABwBgAAEAAAAAAAMABwA2AAEAAAAAAAQABwB1AAEAAAAAAAUACwAVAAEAAAAAAAYABwBLAAEAAAAAAAoAGgCKAAMAAQQJAAEADgAHAAMAAQQJAAIADgBnAAMAAQQJAAMADgA9AAMAAQQJAAQADgB8AAMAAQQJAAUAFgAgAAMAAQQJAAYADgBSAAMAAQQJAAoANACkaWNvbW9vbgBpAGMAbwBtAG8AbwBuVmVyc2lvbiAxLjAAVgBlAHIAcwBpAG8AbgAgADEALgAwaWNvbW9vbgBpAGMAbwBtAG8AbwBuaWNvbW9vbgBpAGMAbwBtAG8AbwBuUmVndWxhcgBSAGUAZwB1AGwAYQByaWNvbW9vbgBpAGMAbwBtAG8AbwBuRm9udCBnZW5lcmF0ZWQgYnkgSWNvTW9vbi4ARgBvAG4AdAAgAGcAZQBuAGUAcgBhAHQAZQBkACAAYgB5ACAASQBjAG8ATQBvAG8AbgAuAAAAAwAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA==) format('woff');
      font-weight: normal;
      font-style: normal;
    }
  </style>
</custom-style>`;

document.head.appendChild($_documentContainer.content);
/**
 *
 * `<vcf-multi-select>` is a Web Component for selecting multiple values from a list of items.
 * 
 * By default, only the first selected value is displayed in the field, with the number of 
 * additionally selcted values (N) is indicated as "(+N other(s))". This behavior can be changed
 * by setting the displayAllSelected property to true, in which case all selected items will be
 * displayed, comma-separated, with ellipsis if more items are present than fits the component.
 * 
 * The content of the the select can be populated in two ways: imperatively by using renderer 
 * callback function and declaratively by using Polymer's Templates.
 *
 * ### Rendering
 *
 * By default, the select uses the content provided by using the renderer callback function.
 *
 * The renderer function provides `root`, `select` arguments.
 * Generate DOM content, append it to the `root` element and control the state
 * of the host element by accessing `select`.
 *
 * ```html
 * <vcf-multi-select id="select"></vcf-multi-select>
 * ```
 * ```js
 * const select = document.querySelector('#select');
 * select.renderer = function(root, select) {
 *   const listBox = document.createElement('vaadin-list-box');
 *   // append 3 <vaadin-item> elements
 *   ['Jose', 'Manolo', 'Pedro'].forEach(function(name) {
 *     const item = document.createElement('vaadin-item');
 *     item.textContent = name;
 *     listBox.appendChild(item);
 *   });
 *
 *   // update the content
 *   root.appendChild(listBox);
 * };
 * ```
 *
 * Renderer is called on initialization of new select and on its opening.
 * DOM generated during the renderer call can be reused
 * in the next renderer call and will be provided with the `root` argument.
 * On first call it will be empty.
 *
 * ### Polymer Templates
 *
 * Alternatively, the content can be provided with Polymer's Template.
 * Select finds the first child template and uses that in case renderer callback function
 * is not provided. You can also set a custom template using the `template` property.
 *
 * ```
 * <vcf-multi-select>
 *   <template>
 *     <vaadin-list-box>
 *       <vaadin-item label="foo">Foo</vaadin-item>
 *       <vaadin-item>Bar</vaadin-item>
 *       <vaadin-item>Baz</vaadin-item>
 *     </vaadin-list-box>
 *   </template>
 * </vcf-multi-select>
 * ```
 *
 * Hint: By setting the `label` property of inner vaadin-items you will
 * be able to change the visual representation of the selected value in the input part.
 *
 * ### Styling
 *
 * The following shadow DOM parts are available for styling:
 *
 * Part name | Description
 * ----------------|----------------
 * `toggle-button` | The toggle button
 * `value-postfix` | The span indicating the number of additional selected values
 *
 * The following state attributes are available for styling:
 *
 * Attribute    | Description | Part name
 * -------------|-------------|------------
 * `opened` | Set when the select is open | :host
 * `invalid` | Set when the element is invalid | :host
 * `focused` | Set when the element is focused | :host
 * `focus-ring` | Set when the element is keyboard focused | :host
 * `readonly` | Set when the select is read only | :host
 * `display-all-selected` | Set when all selected items are shown, comma-separated, rather than being abbreviated (the default) | :host
 *
 * `<vcf-multi-select>` element sets these custom CSS properties:
 *
 * Property name | Description | Theme for element
 * --- | --- | ---
 * `--vcf-multi-select-text-field-width` | Width of the select text field | `vcf-multi-select-overlay`
 *
 * See [ThemableMixin – how to apply styles for shadow parts](https://github.com/vaadin/vaadin-themable-mixin/wiki)
 *
 * In addition to `<vcf-multi-select>` itself, the following internal
 * components are themable:
 *
 * - `<vcf-multi-select-text-field>`
 * - `<vcf-multi-select-overlay>`
 *
 * Note: the `theme` attribute value set on `<vcf-multi-select>` is
 * propagated to the internal themable components listed above.
 *
 * @extends PolymerElement
 * @mixes ElementMixin
 * @mixes ControlStateMixin
 * @mixes ThemableMixin
 * @demo demo/index.html
 */
class VcfMultiSelectElement extends
  ElementMixin(
    ControlStateMixin(
      ThemableMixin(
        mixinBehaviors(IronResizableBehavior, PolymerElement)))) {
  static get template() {
    return html`
    <style>
      :host {
        display: inline-block;
      }

      vcf-multi-select-text-field {
        width: 100%;
        min-width: 0;
      }

      :host([hidden]) {
        display: none !important;
      }

      [part="toggle-button"] {
        font-family: "vcf-multi-select-icons";
      }

      [part="toggle-button"]::before {
        content: "\\e900";
      }

      [part="value-postfix"] {
         font-size: var(--lumo-font-size-xxs);
         padding-left: 0.5em;
         opacity: 0.5;
      }

      :host([display-all-selected]) [part="value"] {        
         --_lumo-text-field-overflow-mask-image: none;
      }
      
      :host([display-all-selected]) [part="value"] vaadin-item {
         display: block;
         overflow: hidden;
      }

    </style>

    <vcf-multi-select-text-field placeholder="[[placeholder]]" label="[[label]]" required="[[required]]" invalid="[[invalid]]" error-message="[[errorMessage]]" readonly\$="[[readonly]]" helper-text="[[helperText]]" theme\$="[[theme]]">
      <slot name="prefix" slot="prefix"></slot>
      <slot name="helper" slot="helper">[[helperText]]</slot>
      <div part="value"></div>
      <div part="toggle-button" slot="suffix" role="button" aria-haspopup="listbox" aria-label="Toggle"></div>
    </vcf-multi-select-text-field>
    <vcf-multi-select-overlay opened="{{opened}}" with-backdrop="[[_phone]]" phone\$="[[_phone]]" theme\$="[[theme]]"></vcf-multi-select-overlay>

    <iron-media-query query="[[_phoneMediaQuery]]" query-matches="{{_phone}}"></iron-media-query>
`;
  }

  static get is() {
    return 'vcf-multi-select';
  }

  static get version() {
    return '1.2.0';
  }

  static get properties() {
    return {
      /**
       * Set when the select is open
       * @type {boolean}
       */
      opened: {
        type: Boolean,
        value: false,
        notify: true,
        reflectToAttribute: true,
        observer: '_openedChanged'
      },

      /**
       * Custom function for rendering the content of the `<vcf-multi-select>`.
       * Receives two arguments:
       *
       * - `root` The `<vcf-multi-select-overlay>` internal container
       *   DOM element. Append your content to it.
       * - `select` The reference to the `<vcf-multi-select>` element.
       * @type {!SelectRenderer | undefined}
       */
      renderer: Function,

      /**
       * The error message to display when the select value is invalid
       * @attr {string} error-message
       * @type {string}
       */
      errorMessage: {
        type: String,
        value: ''
      },

      /**
       * String used for the label element.
       */
      label: {
        type: String
      },

      /**
        * An array that contains the indexes of the items selected in the items array
        * @type {Array<String>}
        */
      selectedIndexes: {
        type: Object,
        value: () => [],
        notify: true,
      },

      /**
       * The current required state of the select. True if required.
       */
      required: {
        type: Boolean,
        reflectToAttribute: true,
        observer: '_requiredChanged'
      },

      /**
       * Set to true if the value is invalid.
       * @type {boolean}
       */
      invalid: {
        type: Boolean,
        reflectToAttribute: true,
        notify: true,
        value: false
      },

      /**
       * The name of this element.
       */
      name: {
        type: String,
        reflectToAttribute: true
      },

      /**
       * A hint to the user of what can be entered in the control.
       * The placeholder will be displayed in the case that there
       * is no item selected, or the selected item has an empty
       * string label, or the selected item has no label and it's
       * DOM content is empty.
       */
      placeholder: {
        type: String
      },

      /**
       * String used for the helper text.
       * @attr {string} helper-text
       */
      helperText: {
        type: String,
        value: ''
      },

      /**
       * When present, it specifies that the element is read-only.
       * @type {boolean}
       */
      readonly: {
        type: Boolean,
        value: false,
        reflectToAttribute: true
      },

      /**
       * If set to true, all selected items will shown comma-separated.
       * 
       * When set to false (default), it specifies that after the first selected item,
       * additional selected items will be abbreviated (showing only the number of 
       * additonally selected items between brackets). 
       * 
       * See also extraItemsCountText property.
       * 
       * @type {boolean}
       */
      displayAllSelected: {
        type: Boolean,
        value: false,
        reflectToAttribute: true,
        observer: '_displayAllSelectedChanged'
      },

      /**
       * Array of Strings used to indicate the number of additionally selected items.
       * The first String is used when there is a single additional item. The second 
       * is used when there two or more additional items are selected.
       * 
       * This property has an effect only when displayAllSelected is false.
       * 
       * @type {Array}
       */
      extraItemsCountText: {
        type: Array,
        value: () => ["other", "others"]
      },

      /** @private */
      _phone: Boolean,

      /** @private */
      _labelItem: Boolean,

      /** @private */
      _phoneMediaQuery: {
        value: '(max-width: 420px), (max-height: 420px)'
      },

      /** @private */
      _overlayElement: Object,

      /** @private */
      _inputElement: Object,

      /** @private */
      _toggleElement: Object,

      /** @private */
      _items: Object,

      /** @private */
      _contentTemplate: Object,

      /** @private */
      _oldTemplate: Object,

      /** @private */
      _oldRenderer: Object
    };
  }

  static get observers() {
    return [
      '_updateSelectedIndexes(selectedIndexes, _items)',
      '_updateAriaExpanded(opened, _toggleElement, _inputElement)',
      '_templateOrRendererChanged(_contentTemplate, renderer, _overlayElement)',
      '_selectedIndexesChanged(selectedIndexes.*)'
    ];
  }

  constructor() {
    super();
    this._boundSetPosition = this._setPosition.bind(this);
  }

  /** @protected */
  connectedCallback() {
    super.connectedCallback();
    this.addEventListener('iron-resize', this._boundSetPosition);
  }

  /** @protected */
  ready() {
    super.ready();

    this._overlayElement = this.shadowRoot.querySelector('vcf-multi-select-overlay');
    this._valueElement = this.shadowRoot.querySelector('[part="value"]');
    this._toggleElement = this.shadowRoot.querySelector('[part="toggle-button"]');
    this._nativeInput = this.focusElement.shadowRoot.querySelector('input');
    this._nativeInput.setAttribute('aria-hidden', true);
    this._nativeInput.setAttribute('tabindex', -1);
    this._nativeInput.style.pointerEvents = 'none';

    this.focusElement.addEventListener('click', e => this.opened = !this.readonly);
    this.focusElement.addEventListener('keydown', e => this._onKeyDown(e));

    this._observer = new FlattenedNodesObserver(this, info => this._setTemplateFromNodes(info.addedNodes));
    this._observer.flush();
  }

  /** @private */
  _setTemplateFromNodes(nodes) {
    const template = Array.from(nodes).filter(node => node.localName && node.localName === 'template')[0] || this._contentTemplate;
    this._overlayElement.template = this._contentTemplate = template;
    this._setForwardHostProps();
  }

  /** @private */
  _setForwardHostProps() {
    if (this._overlayElement.content) {
      const origForwardHostProp = this._overlayElement._instance && this._overlayElement._instance.forwardHostProp;

      if (this._overlayElement._instance) {
        this._overlayElement._instance.forwardHostProp = (...args) => {
          origForwardHostProp.apply(this._overlayElement._instance, args);
          setTimeout(() => {
            this._updateValueSlot();
          });
        };

        this._assignMenuElement();
      }
    }
  }

  /**
   * Manually invoke existing renderer.
   */
  render() {
    this._overlayElement.render();
    if (this._menuElement && this._menuElement.items) {
      this._updateSelectedIndexes(this.selectedIndexes, this._menuElement.items);
    }
  }

  /** @private */
  _removeNewRendererOrTemplate(template, oldTemplate, renderer, oldRenderer) {
    if (template !== oldTemplate) {
      this._contentTemplate = undefined;
    } else if (renderer !== oldRenderer) {
      this.renderer = undefined;
    }
  }

  /** @private */
  _templateOrRendererChanged(template, renderer, overlay) {
    if (!overlay) {
      return;
    }

    if (template && renderer) {
      this._removeNewRendererOrTemplate(template, this._oldTemplate, renderer, this._oldRenderer);
      throw new Error('You should only use either a renderer or a template for select content');
    }

    this._oldTemplate = template;
    this._oldRenderer = renderer;

    if (renderer) {
      overlay.setProperties({owner: this, renderer: renderer});
      this.render();

      if (overlay.content.firstChild) {
        this._assignMenuElement();
      }
    }
  }

  /** @private */
  _assignMenuElement() {
    this._menuElement = Array.from(this._overlayElement.content.children).filter(element => element.localName !== 'style')[0];

    if (this._menuElement) {
      this._menuElement.multiple = true;
      this._menuElement.addEventListener('items-changed', e => {
        this._items = this._menuElement.items;
        this._items.forEach(item => item.setAttribute('role', 'option'));
      });
      this._menuElement.addEventListener('selected-values-changed', e => this._updateValueSlot());
      this._menuElement.addEventListener('keydown', e => this._onKeyDownInside(e));
      this._menuElement.addEventListener('click', e => {
        this.__userInteraction = true;
      }, true);

      this._menuElement.setAttribute('role', 'listbox');
    }
  }

  /**
   * @return {!HTMLElement}
   * @protected
   */
  get focusElement() {
    return this._inputElement ||
      (this._inputElement = this.shadowRoot.querySelector('vcf-multi-select-text-field'));
  }

  /** @protected */
  disconnectedCallback() {
    super.disconnectedCallback();
    this.removeEventListener('iron-resize', this._boundSetPosition);
    // Making sure the select is closed and removed from DOM after detaching the select.
    this.opened = false;
  }

  notifyResize() {
    super.notifyResize();
    if (this.positionTarget && this.opened) {
      this._setPosition();
      // Schedule another position update (to cover virtual keyboard opening for example)
      requestAnimationFrame(this._setPosition.bind(this));
    }
  }

  setExtraItemsCountText(singularString, pluralString){
    this.extraItemsCountText = [singularString, pluralString];
    if (!this.displayAllSelected){
      this._updateValueSlot();
    }
  }

  getExtraItemsCountText(){
    return this.extraItemsCountText;
  }

  /** @private */
  _requiredChanged(required) {
    this.setAttribute('aria-required', required);
  }

  /** @private */
  _displayAllSelectedChanged(displayAllSelected) {
    if (this._valueElement && this.selectedIndexes.length > 0) {
      this._updateValueSlot();
    }
  }

  /** @private */
  _selectedIndexesChanged(e) {
    if (!this.selectedIndexes) {
      return;
    }
    if (this.selectedIndexes.length == 0) {
      this.focusElement.removeAttribute('has-value');
    } else {
      this.focusElement.setAttribute('has-value', '');
    }
  }

  /** @private */
  _valueChanged(value, oldValue) {
    if (value === []) {
      this.focusElement.removeAttribute('has-value');
    } else {
      this.focusElement.setAttribute('has-value', '');
    }

    // Skip validation for the initial empty string value
    if (value === [] && oldValue === undefined) {
      return;
    }
    this.validate();
  }

  /**
   * @param {!KeyboardEvent} e
   * @protected
   */
  _onKeyDown(e) {
    if (!this.readonly && !this.opened) {
      if (/^(Enter|SpaceBar|\s|ArrowDown|Down|ArrowUp|Up)$/.test(e.key)) {
        e.preventDefault();
        this.opened = true;

      } else if (/[a-zA-Z0-9]/.test(e.key) && e.key.length === 1) {
        const selected = this._menuElement.selected;
        const currentIdx = selected !== undefined ? selected : -1;
        const newIdx = this._menuElement._searchKey(currentIdx, e.key);
        if (newIdx >= 0) {
          this.__userInteraction = true;
          this._updateSelectedIndexes(newIdx, this._items);
        }
      }
    }
  }

  /**
   * @param {!KeyboardEvent} e
   * @protected
   */
  _onKeyDownInside(e) {
    if (/^(Tab)$/.test(e.key)) {
      this.opened = false;
    }
  }

  /** @private */
  _openedChanged(opened, wasOpened) {
    if (opened) {
      if (
        !this._overlayElement ||
        !this._menuElement ||
        !this._toggleElement ||
        !this.focusElement ||
        this.disabled ||
        this.readonly
      ) {
        this.opened = false;
        return;
      }

      this._openedWithFocusRing = this.hasAttribute('focus-ring') || this.focusElement.hasAttribute('focus-ring');
      this._menuElement.focus();
      this._setPosition();
      window.addEventListener('scroll', this._boundSetPosition, true);
    } else if (wasOpened) {
      if (this._phone) {
        this._setFocused(false);
      } else {
        this.focusElement.focus();
        if (this._openedWithFocusRing) {
          this.focusElement.setAttribute('focus-ring', '');
        }
      }
      this.validate();
      window.removeEventListener('scroll', this._boundSetPosition, true);
    }
  }

  /** @private */
  _hasContent(selectedIndexes) {
    if (!selectedIndexes || selectedIndexes.length==0) {
      return false;
    } else {
        return true;
    }
  }

  /** @private */
  _updateTextField(selectedIndexes) {
    if (!this.displayAllSelected) {
      const newTextContentAbbreviated =  this._getTextContentAbbreviated(selectedIndexes);
      this._updateTextFieldAsAbbreviated(newTextContentAbbreviated);
    } else {
      const newTextContentAll = this._getTextContentAll(selectedIndexes);
      this._updateTextFieldAsAll(newTextContentAll);
    }  
  }

  /** @private */
  _getTextContentAbbreviated(selectedIndexes){
    let mainValue = this._items[selectedIndexes[0]].innerText;
    let valuePostfix;

    const remainder = selectedIndexes.length - 1;
    if (remainder > 0) {
    valuePostfix = "(+" + remainder + " " ;
    if (remainder == 1) {
        valuePostfix += this.extraItemsCountText[0];
    } else {
        valuePostfix += this.extraItemsCountText[1];
    }
    valuePostfix += ")";
    }

    return {
    mainValue: mainValue,
    valuePostfix: valuePostfix,
    };
  }

  /** @private */
  _updateTextFieldAsAbbreviated(newTextContent) {
    if (!this._labelItem){
      this._labelItem = document.createElement('vaadin-item');
      this._labelItem.removeAttribute('tabindex');
      this._labelItem.removeAttribute('role');
    }
    this._labelItem.textContent = newTextContent.mainValue;

    if (newTextContent.valuePostfix) {
      let span = document.createElement('span');
      span.innerText = newTextContent.valuePostfix;
      span.setAttribute('part', "value-postfix");
      this._labelItem.appendChild(span);
    }

    this._labelItem.selected = true;
    this._valueElement.appendChild(this._labelItem);    
  }

  /** @private */
  _getTextContentAll(selectedIndexes){
    let arrValues = [];
    for (let i = 0; i < selectedIndexes.length; i++) {
        arrValues.push(this._items[selectedIndexes[i]].innerText);
    }
    return arrValues.join(", ");
  }

  /** @private */
  _updateTextFieldAsAll(newTextContent) {
    if (!this._labelItem){
      this._labelItem = document.createElement('vaadin-item');
      this._labelItem.removeAttribute('tabindex');
      this._labelItem.removeAttribute('role');
    }
    this._labelItem.textContent = newTextContent;

    this._labelItem.selected = true;
    this._valueElement.appendChild(this._labelItem);

    let itemStyle = this._labelItem.shadowRoot.querySelector('[part = content]').style;
    itemStyle.setProperty('text-overflow', 'ellipsis');
    itemStyle.setProperty('overflow', 'hidden');
  }

  /** @private */
  _updateAriaExpanded(opened, toggleElement, inputElement) {
    toggleElement && toggleElement.setAttribute('aria-expanded', opened);
    if (inputElement && inputElement.focusElement) {
      inputElement.focusElement.setAttribute('aria-expanded', opened);
    }
  }

  /** @private */
  _updateValueSlot() {
    this._valueElement.innerHTML = '';

    const selectedIndexes = this._menuElement.selectedValues;

    const hasContent = this._hasContent(selectedIndexes);

    // Check if text-field is using slotted input
    const slotName = this._inputElement.shadowRoot.querySelector('slot[name="input"]') ? 'input' : 'value';

    // Toggle visibility of _valueElement vs fallback input with placeholder
    this._valueElement.slot = hasContent ? slotName : '';

    // Ensure the slot distribution to apply correct style scope for cloned item
    if (hasContent && window.ShadyDOM) {
      window.ShadyDOM.flush();
    }

    if (hasContent){
        this._updateTextField(selectedIndexes);
    }
      

    if (!this._valueChanging && selectedIndexes) {
      this._selectedChanging = true;
      this.selectedIndexes = selectedIndexes;
      if (this.__userInteraction) {
        this.dispatchEvent(new CustomEvent('change', {bubbles: true}));
        this.__userInteraction = false;
      }
      delete this._selectedChanging;
    }
  }

  /** @private */
  _updateSelectedIndexes(indexes, items) {
    if (!indexes){
      return;
    }
    if (items) {
      this.selectedIndexes = indexes;
      this._menuElement.selectedValues = indexes;

      if (!this._selectedChanging) {
        this._valueChanging = true;
        this._updateValueSlot();
        delete this._valueChanging;
      }
    }
  }

  /**
   * @param {boolean} focused
   * @protected
   */
  _setFocused(focused) {
    // Keep `focused` state when opening the overlay for styling purpose.
    super._setFocused(this.opened || focused);
    this.focusElement._setFocused(this.hasAttribute('focused'));
    !this.hasAttribute('focused') && this.validate();
  }

  /** @private */
  _setPosition() {
    const inputRect = this._inputElement.shadowRoot.querySelector('[part~="input-field"]').getBoundingClientRect();
    const viewportHeight = Math.min(window.innerHeight, document.documentElement.clientHeight);
    const bottomAlign = inputRect.top > (viewportHeight - inputRect.height) / 2;

    const isRtl = this.getAttribute('dir') === 'rtl';
    if (isRtl) {
      this._overlayElement.style.right = document.documentElement.clientWidth - inputRect.right + 'px';
    } else {
      this._overlayElement.style.left = inputRect.left + 'px';
    }

    if (bottomAlign) {
      this._overlayElement.setAttribute('bottom-aligned', '');
      this._overlayElement.style.removeProperty('top');
      this._overlayElement.style.bottom = (viewportHeight - inputRect.bottom) + 'px';
    } else {
      this._overlayElement.removeAttribute('bottom-aligned');
      this._overlayElement.style.removeProperty('bottom');
      this._overlayElement.style.top = inputRect.top + 'px';
    }

    this._overlayElement.updateStyles({'--vcf-multi-select-text-field-width': inputRect.width + 'px'});
  }

  /**
   * Returns true if `value` is valid, and sets the `invalid` flag appropriately.
   *
   * @return {boolean} True if the value is valid and sets the `invalid` flag appropriately
   */
  validate() {
    return !(this.invalid = !(this.disabled || !this.required || this.selectedIndexes.length));
  }

  static _finalizeClass() {
    super._finalizeClass();
  }

  /**
   * Fired when the user commits a value change.
   *
   * @event change
   * @event selected-indexes-changed
   */
}

customElements.define(VcfMultiSelectElement.is, VcfMultiSelectElement);

export { VcfMultiSelectElement };
