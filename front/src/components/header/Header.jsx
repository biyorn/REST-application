import React from 'react'
import { Navbar } from 'react-bootstrap'
import { Nav } from 'react-bootstrap'
import { Button } from 'react-bootstrap'
import './Header.css'
import LanguageDropdown from './language/LanguageDropdown'
import createTokenProvider from '../login/token/CreateTokenProvider'
import createAuthProvider from '../login/auth/CreateAuthProvider'
import { withRouter } from 'react-router-dom'
import locale from '../header/language/Localization';

const tokenProvider = createTokenProvider();
const roles = tokenProvider.getRoles();
const admin = roles.includes('ROLE_ADMIN');
const name = tokenProvider.getName();

class Header extends React.Component {

    handleLogout = (e) => {
        e.preventDefault();
        createAuthProvider().logout();
        this.props.history.push('/login');
    }

    addButton = () => {
        return (
            this.props.logged || admin
                ?
                <Navbar.Collapse id="basic-navbar-nav" className="nav-add">
                    <Nav className="mr-auto">
                        <Button variant="outline-primary" className="nav-add-button" href="/add">
                            {locale.getString('addNew')}
                        </Button>
                    </Nav>
                </Navbar.Collapse>
                : ''
        )
    }

    basketButton = () => {
        return (
            this.props.logged || tokenProvider.isLoggedIn()
                ?
                (
                    <Nav.Link href="/basket" onClick={this.handleBasket} className="mr-sm-5">
                        <img className="basket-img" src="/image/basket.png" />
                    </Nav.Link>
                )
                : ''
        )
    }

    username = () => {
        return (
            this.props.logged || tokenProvider.isLoggedIn()
                ?
                <Navbar.Brand className="mr-sm-5">
                    {name}
                </Navbar.Brand>
                : ''
        )
    }

    signInOrSignOut = () => {
        if (this.props.logged || tokenProvider.isLoggedIn()) {
            return (
                <Nav.Link href="/logout" onClick={this.handleLogout} className="mr-sm-2">
                    {locale.getString('signOut')}
                </Nav.Link>
            )
        } else {
            return (
                <Nav.Link href="/login" className="mr-sm-2">{locale.getString('signIn')}</Nav.Link>
            )
        }
    }

    signUpButton = () => {
        return (
            !this.props.logged && !tokenProvider.isLoggedIn()
                ?
                <Button href="/sign-up" className="mr-sm-2">
                    {locale.getString('signUp')}
                </Button>
                : ''
        )
    }

    render() {
        return (
            <Navbar bg="light" expand="lg" fixed="top" className="shadow-sm p-2 mb-2 bg-white rounded">
                <Navbar.Brand href="/certificates" className="ml-sm-3">Certificates</Navbar.Brand>
                {this.addButton()}
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="ml-auto">
                        <LanguageDropdown />
                        {this.basketButton()}
                        {this.username()}
                        {this.signInOrSignOut()}
                        {this.signUpButton()}
                    </Nav>
                </Navbar.Collapse>
            </Navbar>
        )
    }
}

export default withRouter(Header);