import React from 'react'
import './SignUp.css'
import { Container, Row, Col, Form, Button } from 'react-bootstrap';
import PageTitle from '../../components/page-title/PageTitle'
import Axios from 'axios'
import { withRouter } from 'react-router-dom'
import ErrorAlert from '../alerts/ErrorAlert';
import locale from '../header/language/Localization'
import { verifyLogin } from '../validation/user/LoginValidation'
import { verifyPassword } from '../validation/user/PasswordValidation'
import { verifyName } from '../validation/user/NameValidation'
import { verifySurname } from '../validation/user/SurnameValidation'
import signUp from './service/SignUpRequest'

class SignUp extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            login: '',
            password: '',
            name: '',
            surname: '',
            loginValid: false,
            passwordValid: false,
            nameValid: false,
            surnameValid: false,
            errorMessage: ''
        }
    }

    changeHandler = (e) => {
        this.setState({ [e.target.name]: e.target.value })
        const value = e.target.value;
        const name = e.target.name;
        switch (name) {
            case 'login':
                this.loginValid(value)
                break;
            case 'password':
                this.passwordValid(value);
                break;
            case 'name':
                this.nameValid(value);
                break;
            case 'surname':
                this.surnameValid(value);
                break;
        }
    }

    loginValid = (value) => {
        const result = verifyLogin(value)
        !result ? this.setState({ loginValid: false }) : this.setState({ loginValid: true })
    }

    passwordValid = (value) => {
        const result = verifyPassword(value);
        !result ? this.setState({ passwordValid: false }) : this.setState({ passwordValid: true })
    }

    nameValid = (value) => {
        const result = verifyName(value);
        !result ? this.setState({ nameValid: false }) : this.setState({ nameValid: true })
    }

    surnameValid = (value) => {
        const result = verifySurname(value);
        !result ? this.setState({ surnameValid: false }) : this.setState({ surnameValid: true })
    }

    submitHandler = (e) => {
        e.preventDefault();
        this.setState({ errorMessage: '' })
        const login = this.state.login;
        const pass = this.state.password;
        const name = this.state.name;
        const surname = this.state.surname;
        signUp(login, pass, name, surname).then(response => {
            this.props.history.push('/login')
        }).catch(error => {
            this.setState({
                errorMessage: error.response.data.message,
                login: '',
                password: '',
                name: '',
                surname: '',
                loginValid: false,
                passwordValid: false,
                nameValid: false,
                surnameValid: false
            })
        })
    }

    closeAlert = () => {
        this.setState({ errorMessage: '' })
    }

    render() {
        const isDisabled =
            !this.state.loginValid ||
            !this.state.passwordValid ||
            !this.state.nameValid ||
            !this.state.surnameValid;
        const {
            login,
            password,
            name,
            surname,
            loginValid,
            passwordValid,
            nameValid,
            surnameValid,
            errorMessage
        } = this.state;
        return (
            <Container>
                <Row>
                    {errorMessage != ''
                        ? <ErrorAlert close={this.closeAlert} message={errorMessage} />
                        : <p className="empty-title">&nbsp;</p>}
                </Row>
                <Row>
                    <Col>
                        <PageTitle title={locale.getString('certificates')} />
                    </Col>
                </Row>
                <Row className="sign-up-main">
                    <Col sm={5}>
                        <Container>
                            <Row>
                                <Col className="sign-up-item">
                                    <Form.Group controlId="formGroupEmail">
                                        <Form.Label className="Label">{locale.getString('login')}</Form.Label>
                                        <Form.Control
                                            title={!loginValid ? locale.getString('loginMessage') : ''}
                                            type="login" name="login"
                                            placeholder={locale.getString('enterYourLogin')}
                                            value={login}
                                            className="Control" onChange={this.changeHandler}
                                            isValid={loginValid} isInvalid={login != '' && !loginValid}
                                        />
                                    </Form.Group>
                                </Col>
                            </Row>
                            <Row>
                                <Col className="sign-up-item">
                                    <Form.Group controlId="formGroupEmail">
                                        <Form.Label className="Label">{locale.getString('password')}</Form.Label>
                                        <Form.Control
                                            title={!passwordValid ? locale.getString('passwordMessage') : ''}
                                            type="password" name="password"
                                            placeholder={locale.getString('enterYourPassword')}
                                            maxlength={8}
                                            value={password}
                                            className="Control" onChange={this.changeHandler}
                                            isValid={passwordValid} isInvalid={password != '' && !passwordValid}
                                        />
                                    </Form.Group>
                                </Col>
                            </Row>
                            <Row>
                                <Col className="sign-up-item">
                                    <Form.Group controlId="formGroupEmail">
                                        <Form.Label className="Label">{locale.getString('firstName')}</Form.Label>
                                        <Form.Control
                                            title={!nameValid ? locale.getString('firstNameMessage') : ''}
                                            type="name" name="name"
                                            value={name}
                                            placeholder={locale.getString('firstNameEnt')}
                                            className="Control" onChange={this.changeHandler}
                                            isValid={nameValid} isInvalid={name != '' && !nameValid}
                                        />
                                    </Form.Group>
                                </Col>
                            </Row>
                            <Row>
                                <Col className="sign-up-item">
                                    <Form.Group controlId="formGroupEmail">
                                        <Form.Label className="Label">{locale.getString('lastName')}</Form.Label>
                                        <Form.Control
                                            title={!surnameValid ? locale.getString('lastNameMessage') : ''}
                                            type="surname" name="surname"
                                            value={surname}
                                            placeholder={locale.getString('lastNameEnt')}
                                            className="Control" onChange={this.changeHandler}
                                            isValid={surnameValid} isInvalid={surname != '' && !surnameValid}
                                        />
                                    </Form.Group>
                                </Col>
                            </Row>
                            <Row>
                                <Col>
                                    <Button variant="outline-success" onClick={this.submitHandler}
                                        disabled={isDisabled}>
                                        {locale.getString('signUp')}
                                    </Button>
                                </Col>
                            </Row>
                        </Container>
                    </Col>
                    <Col sm={7}>
                        <img src="/image/welcome.jpg" />
                    </Col>
                </Row>
            </Container>
        )
    }
}

export default withRouter(SignUp);