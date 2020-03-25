import React from 'react';
import './Certificate.css';
import Tag from '../../tag/Tag'
import { Col, Button } from 'react-bootstrap';
import { Row } from 'react-bootstrap';
import { Modal, Container } from 'react-bootstrap';
import { withRouter } from 'react-router-dom'
import { deleteCertificate } from '../service/DeleteCertificate'
import DeleteModal from '../../edit-certificate/modal/DeleteModal'
import basketProvider from '../basket/BasketProvider'
import createTokenProvider from '../../login/token/CreateTokenProvider'
import locale from '../../header/language/Localization'
import queryString from 'query-string'

const basket = basketProvider();
const tokenProvider = createTokenProvider();
const roles = tokenProvider.getRoles();
const admin = roles.includes('ROLE_ADMIN')
const user = roles.includes('ROLE_USER')

class Certificate extends React.Component {

    componentWillMount() {
        this.setState({
            certificate: this.props.certificate,
            id: this.props.id,
            name: this.props.name,
            desc: this.props.desc,
            price: this.props.price,
            date: this.props.date,
            tags: this.props.tags,
            active: this.props.active
        })
    }

    handleTitleClick = () => {
        this.setState({ titleActive: true })
    }

    handleTitleClose = () => {
        this.setState({ titleActive: false })
    }

    handleDeleteClick = () => {
        this.setState({ deleteActive: true })
    }

    handleDeleteClose = () => {
        this.setState({ deleteActive: false })
    }

    handleDeleteCertificate = (e) => {
        e.preventDefault();
        let parameters = queryString.parse(this.props.location.search);
        deleteCertificate(this.state.id).then(response => {
            this.props.history.push(this.props.location.pathname + '?' + queryString.stringify(parameters),
                ({ successMessage: locale.getString('deleteCertificate') }))
        }).catch(error => {
            this.setState({ errorMessage: error.response })
        })
    }

    addToBasket = (e) => {
        e.preventDefault();
        const { certificate } = this.state;
        basket.addToBasket(certificate);
    }

    toBasketButton = () => {
        return (
            user ?
                <Button variant="danger" onClick={this.addToBasket}>
                    {locale.getString('toBasket')}
                </Button>
                : ''
        )
    }

    deleteButton = () => {
        return (
            admin
                ?
                <Button name="deleteActive" className="delete-button" variant="danger" onClick={this.handleDeleteClick}>
                    {locale.getString('delete')}
                </Button>
                : ''
        )
    }

    deleteModal = () => {
        return (
            this.state.deleteActive ? <DeleteModal question={locale.getString('deleteQuestion')}
                handleClose={this.handleDeleteClose}
                handleDelete={this.handleDeleteCertificate} active={true} id={this.state.id} /> : ''
        )
    }

    editButton = () => {
        return (
            admin
                ?
                <Button className="edit-button" variant="danger" href={'/edit/' + this.state.id} value={this.state.id}>
                    {locale.getString('edit')}
                </Button>
                : ''
        )
    }

    tagIcons = () => {
        return (
            this.state.tags.map(tag =>
                <Col>
                    <Tag title={tag.title} />
                </Col>
            )
        )
    }

    render() {
        return (
            <div class="card">
                <div className="title">
                    <Row>
                        <Col className="title-text">
                            <button name="titleActive" onClick={this.handleTitleClick} className="title-button">
                                {this.props.name}
                            </button>
                            <Modal show={this.state.titleActive} onHide={this.handleTitleClose}>
                                <Modal.Header closeButton>
                                    <Modal.Title>
                                        <Row className="modal-name">{this.state.name} </Row>
                                        <Row className="modal-date">
                                            {locale.getString('creationDate')}: {this.state.date}
                                        </Row>
                                    </Modal.Title>
                                </Modal.Header>
                                <Modal.Body>
                                    <Container className="modal-container">
                                        <Row className="modal-tags">
                                            {this.state.tags.map(tag =>
                                                <Col>
                                                    <Tag title={tag.title} />
                                                </Col>
                                            )}
                                        </Row>
                                        <Row className="modal-desc">
                                            <p>{this.state.desc}</p>
                                        </Row>
                                    </Container>
                                </Modal.Body>
                                <Modal.Footer>
                                    <Container>
                                        <Row>
                                            <Col sm={5} className="modal-price">
                                                <p>{this.state.price} BYN</p>
                                            </Col>
                                            <Col sm={4} />
                                            <Col sm={2} className="modal-close">
                                                <Button name="titleActive" variant="secondary" onClick={this.handleTitleClose}>
                                                    {locale.getString('close')}
                                                </Button>
                                            </Col>
                                        </Row>
                                    </Container>
                                </Modal.Footer>
                            </Modal>
                        </Col>
                        <Col className="title-date">
                            <p>{this.state.date}</p>
                        </Col>
                    </Row>
                </div>
                <div className="content">
                    <Container className="Container">
                        <Row className="row-tags">
                            {this.tagIcons()}
                        </Row>
                        <Row className="row-desc">
                            <p>{this.state.desc}</p>
                        </Row>
                    </Container>
                </div>
                <div className="footer">
                    <Container className="footer-container">
                        <Row className="footer-buttons">
                            <Col className="footer-buttons-item">
                                {this.editButton()}
                            </Col>
                            <Col className="footer-buttons-item">
                                {this.deleteButton()}
                            </Col>
                            {this.deleteModal()}
                            <Col className="buy-button footer-buttons-item">
                                {this.toBasketButton()}
                            </Col>
                            <Col className="price footer-buttons-item">
                                <p>{this.state.price} BYN</p>
                            </Col>
                        </Row>
                    </Container>
                </div>
            </div>
        )
    }
}

export default withRouter(Certificate);