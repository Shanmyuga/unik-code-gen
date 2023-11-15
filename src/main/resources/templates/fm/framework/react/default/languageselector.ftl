import React, { Component } from 'react';
import { bindActionCreators } from 'redux';
import { connect } from 'react-redux';
import { SplitButton, MenuItem } from 'react-bootstrap';
import { setLocale } from 'react-redux-i18n';

class LanguageSelector extends Component {
    constructor(props) {
        super(props);

        this.changeLang = this.changeLang.bind(this);
    }

    changeLang(e) {
        this.props.actions.setLocale(e);
    }

    render() {
        return (
              <div className="language-selector">
              <SplitButton
                  bsStyle="primary" onSelect={this.changeLang}
                  title="Language"
                  key="1" pullRight
                  id="languageSelector">
                  <#if languages??>
                  <#list languages?keys as lang>
                  <MenuItem eventKey="${lang}">${languages[lang]}</MenuItem>
                  </#list>
                  </#if>
                  <MenuItem divider />
                </SplitButton>
              </div>
        );
    }
}

function mapDispatchToProps (dispatch) {
  const actions = {
        setLocale
  };
  return {
      actions: bindActionCreators( actions, dispatch)
  };
}

function mapStateToProps (state) {

}

export default connect(mapStateToProps, mapDispatchToProps)(LanguageSelector);