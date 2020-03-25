import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Redirect
} from "react-router-dom";
import Header from './components/header/Header'
import Footer from './components/footer/Footer'
import Login from './components/login/Login'
import CertificatesPage from './components/certificates-page/CertificatesPage'
import EditCertificate from './components/edit-certificate/EditCertificate';
import SignUp from './components/sign-up/SignUp'
import Basket from './components/certificates-page/basket/Basket';
import createTokenProvider from './components/login/token/CreateTokenProvider'
import locale from './components/header/language/Localization'

const tokenProvider = createTokenProvider();
const roles = tokenProvider.getRoles();
const guest = roles.length === 0;
const admin = roles.includes('ROLE_ADMIN')
const user = roles.includes('ROLE_USER');

class App extends React.Component {
  constructor(props) {
    super(props);
    this.state = ({
      logged: false,
      message: '',
      locale: localStorage.getItem('LANGUAGE') ? localStorage.getItem('LANGUAGE') : 'en'
    })
  }

  handleLogin = () => {
    this.setState({
      logged: true
    })
  }

  handleLogout() {
    this.setState({
      logged: false
    })
  }

  updateMessage = (message) => {
    this.setState({
      message: message
    })
  }

  loginPage = () => {
    return (
      <Route path="/login" render={() => (guest ?
        <Login handleLogin={this.handleLogin} /> : <Redirect to="/certificates" />
      )} />
    )
  }

  certificatesPage = () => {
    return (
      <Route exact path="/certificates" component={CertificatesPage} />
    )
  }

  editCertificatePage = () => {
    return (
      <Route path="/edit/:id" render={() => (admin ?
        <EditCertificate title={locale.getString('editCertificate')} />
        : guest
          ? <Redirect to="/login" />
          : <Redirect to="/certificates" />
      )} />
    )
  }

  addPage = () => {
    return (
      <Route path="/add" render={() => (admin ?
        <EditCertificate updateMessage={this.updateMessage} title={locale.getString('addCertificate')} />
        : guest ? <Redirect to="/login" /> : <Redirect to="/certificates" />)}
      />
    )
  }

  basketPage = () => {
    return (
      <Route path="/basket" render={() => (user || admin ?
        <Basket /> : guest ? <Redirect to="/login" /> : <Redirect to="/certificates" />
      )} />
    )
  }

  signUpPage = () => {
    return (
      <Route path="/sign-up" render={() => (guest ?
        <SignUp /> : <Redirect to="/certificates" />
      )} />
    )
  }

  render() {
    tokenProvider.refreshToken();
    return (
      <Router>
        <div className="App">
          <Header />
          <div className="component">
            <Switch>
              {this.loginPage()}

              {this.certificatesPage()}
              {this.editCertificatePage()}
              {this.addPage()}
              {this.basketPage()}
              {this.signUpPage()}
              <Route render={() => <Redirect to="/certificates" />} />
            </Switch>
          </div>
          <Footer />
        </div>
      </Router >
    )
  }
}

export default App;