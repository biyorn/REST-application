import React from 'react'
import { NavDropdown } from 'react-bootstrap'
import locale from '../language/Localization';

const lang = localStorage.getItem('LANGUAGE');
const EN = 'en';
const RU = 'ru';

class LanguageDropdown extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            language: EN
        }
    }

    componentDidMount() {
        this.setState({
            language: lang ? lang : EN
        })
    }

    changeLang = (e) => {
        const value = e.target.value;
        this.setState({ language: value })
        localStorage.setItem('LANGUAGE', value);
        window.location.reload()
    }

    render() {
        const language = this.state.language;
        return (
            <NavDropdown title={locale.getString('language')} id="basic-nav-dropdown" className="mr-sm-5">
                <NavDropdown.Item as="button" active={language === RU} value={RU} onClick={this.changeLang}>
                    {locale.getString('ru')}
                </NavDropdown.Item>
                <NavDropdown.Item as="button" active={language === EN} value={EN} onClick={this.changeLang}>
                    {locale.getString('en')}
                </NavDropdown.Item>
            </NavDropdown>
        )
    }
}

export default LanguageDropdown;