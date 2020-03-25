import React from 'react'
import { Container, Row, Col, Button } from 'react-bootstrap';
import './Basket.css'
import PageTitle from '../../page-title/PageTitle'
import Certificate from '../certificate/Certificate'
import basketProvider from '../basket/BasketProvider'
import { withRouter } from 'react-router-dom'
import SuccessAlert from '../../alerts/SuccessAlert'
import ErrorAlert from '../../alerts/ErrorAlert'
import locale from '../../header/language/Localization'
import DeleteModal from '../../edit-certificate/modal/DeleteModal'

const basketService = basketProvider();

class Basket extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            successMessage: '',
            errorMessage: ''
        }
    }

    componentWillMount() {
        const certificates = basketService.getCertificates();
        this.setState({ certificates })
    }

    buyHandle = () => {
        basketService.buy(this.state.certificates).then(response => {
            basketService.clearBasket();
            this.setState({
                successMessage: locale.getString('buyMessage'),
                certificates: []
            })
        }).catch(error => {
            this.setState({ errorMessage: error.response.data.message })
        })
    }

    clearBasket = () => {
        basketService.clearBasket();
        this.clearClickClose()
        this.setState({
            successMessage: locale.getString('cleanMessage'),
            certificates: []
        })
    }

    clearClickHandle = () => {
        this.setState({ clearActive: true })
    }

    clearClickClose = () => {
        this.setState({ clearActive: false })
    }

    closeAlert = () => {
        this.setState({
            successMessage: '',
            errorMessage: ''
        })
    }

    basketCertificates = () => {
        const { certificates } = this.state;
        return (
            certificates.map(certificate =>
                <Col sm={6}>
                    <Certificate
                        certificate={certificate}
                        id={certificate.id}
                        name={certificate.name}
                        date={certificate.date}
                        desc={certificate.description}
                        price={certificate.price}
                        tags={certificate.tags}
                        active={certificate.active}
                    />
                </Col>
            )
        )
    }

    render() {
        const status = !this.state.certificates.length;
        const alert = this.state.successMessage != ''
            ? <SuccessAlert close={this.closeAlert} message={this.state.successMessage} />
            : this.state.errorMessage != ''
                ? <ErrorAlert close={this.closeAlert} message={this.state.errorMessage} />
                : <p className="empty-title">&nbsp;</p>
        return (
            <Container>
                <Row>
                    {alert}
                </Row>
                <Row>
                    <Col>
                        <PageTitle title={locale.getString('basketTitle')} />
                    </Col>
                </Row>
                <Row className="basket-buy">
                    <Col sm={2} />
                    <Col sm={4}>
                        <Button variant="danger" className={status ? "non-active" : ''} onClick={this.buyHandle}>
                            {locale.getString('buy')}
                        </Button>
                    </Col>
                    <Col sm={4}>
                        <Button variant="secondary" className={status ? "non-active" : ''} onClick={this.clearClickHandle}>
                            {locale.getString('clear')}
                        </Button>
                        {this.state.clearActive ? <DeleteModal question={locale.getString('clearQuestion')}
                            handleClose={this.clearClickClose}
                            handleDelete={this.clearBasket} active={true} id={this.state.id} /> : ''}
                    </Col>
                    <Col sm={2} />
                </Row>
                <Row className="basket-certificates">
                    {this.basketCertificates()}
                </Row>
            </Container>
        )
    }
}

export default withRouter(Basket);