import React from 'react'
import './Login.css'
import PageTitle from '../page-title/PageTitle'
import { Form } from 'react-bootstrap'
import { Button } from 'react-bootstrap'
import { withRouter } from 'react-router-dom'
import createAuthProvider from '../login/auth/CreateAuthProvider'
import ErrorAlert from '../alerts/ErrorAlert'
import locale from '../header/language/Localization'
import { verifyLogin } from '../validation/user/LoginValidation'
import { verifyPassword } from '../validation/user/PasswordValidation'
import { authenticate } from '../login/service/AuthenticateUser'

const authProvider = createAuthProvider();

class Login extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            login: '',
            password: '',
            errorMessage: '',
            loginValid: false,
            passwordValid: false
        };
    }

    changeHandler = (e) => {
        this.setState({ [e.target.name]: e.target.value })
        const name = e.target.name;
        const value = e.target.value;
        switch (name) {
            case 'login':
                this.isValidLogin(value);
                break
            case 'password':
                this.isValidPass(value);
                break;
        }
    }

    isValidLogin = (login) => {
        const result = verifyLogin(login);
        this.setState({ loginValid: result })
    }

    isValidPass = (password) => {
        const result = verifyPassword(password);
        this.setState({ passwordValid: result })
    }

    submitHandler = (e) => {
        e.preventDefault()
        authenticate(this.state).then(response => {
            authProvider.login(response.data);
            this.props.history.push('/certificates')
            this.props.handleLogin();
        }).catch(error => {
            this.setState({
                errorMessage: error.response.data.message,
                login: '',
                password: '',
                loginValid: false,
                passwordValid: false
            })
        })
    }

    closeAlert = () => {
        this.setState({ errorMessage: '' })
    }

    render() {
        const { login, password, loginValid, passwordValid } = this.state
        return (
            <div className="Login-page">
                {this.state.errorMessage != ''
                    ? <ErrorAlert close={this.closeAlert} message={this.state.errorMessage} />
                    : <p className="empty-title">&nbsp;</p>}
                <PageTitle title={locale.getString('certificates')} />
                <div className="login-place">
                    <div className="login-form">
                        <Form className="Form" onSubmit={this.submitHandler}>
                            <Form.Group controlId="formGroupEmail">
                                <Form.Label className="Label">{locale.getString('login')}</Form.Label>
                                <Form.Control
                                    title={!loginValid ? locale.getString('loginMessage') : ''}
                                    type="login" name="login"
                                    value={login} placeholder={locale.getString('enterYourLogin')}
                                    className="Control" onChange={this.changeHandler}
                                    isValid={loginValid} isInvalid={login != '' && !loginValid}
                                />
                            </Form.Group>
                            <Form.Group controlId="formGroupPassword">
                                <Form.Label className="Label">{locale.getString('password')}</Form.Label>
                                <Form.Control
                                    title={!passwordValid ? locale.getString('passwordMessage') : ''}
                                    type="password" name="password"
                                    maxlength={8}
                                    value={password} placeholder={locale.getString('enterYourPassword')}
                                    className="Control" onChange={this.changeHandler}
                                    isValid={passwordValid} isInvalid={password != '' && !passwordValid}
                                />
                            </Form.Group>
                            <Button type="submit" variant="outline-success" className="success-button"
                                disabled={!loginValid || !passwordValid}>
                                {locale.getString('logIn')}
                            </Button>
                            <Button href="/certificates" variant="outline-danger" className="cancel-login-button">
                                {locale.getString('cancel')}
                            </Button>
                        </Form>
                    </div>
                    <div className="login-picture">
                        <img src="/image/welcome.jpg" />
                    </div>
                </div>
            </div>
        )
    }
}

export default withRouter(Login);