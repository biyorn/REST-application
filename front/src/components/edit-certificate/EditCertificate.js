import React from 'react'
import PageTitle from '../page-title/PageTitle'
import Tag from '../edit-certificate/tag/Tag'
import './EditCertificate.css'
import { Container, Row, Col, Form } from 'react-bootstrap'
import { withRouter } from 'react-router-dom'
import { deleteCertificate } from '../certificates-page/service/DeleteCertificate'
import { getCertificateById } from '../certificates-page/service/GetCertificateById'
import { addCertificate } from '../certificates-page/service/AddCertificate'
import { updateCertificate } from '../certificates-page/service/UpdateCertificate'
import DeleteModal from '../../components/edit-certificate/modal/DeleteModal'
import ErrorAlert from '../alerts/ErrorAlert'
import SuccessAlert from '../alerts/SuccessAlert'
import locale from '../header/language/Localization'
import { verifyTitle } from '../validation/certificate/TitleValidation'
import { verifyDescription } from '../validation/certificate/DescriptionValidation'
import { verifyDuration } from '../validation/certificate/DurationValidation'
import { verifyPrice } from '../validation/certificate/PriceValidation'
import { verifyTagName } from '../validation/certificate/TagNameValidation'

class EditCertificate extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            type: '',
            tags: [],
            tagName: '',
            title: '',
            desc: '',
            price: '',
            duration: '',
            titleMessage: '',
            descMessage: '',
            priceMessage: '',
            durationMessage: '',
            tagNameMessage: '',
            titleError: false,
            descError: false,
            priceError: false,
            durationError: false,
            tagNameError: true,
            errorMessage: '',
            successMessage: '',
            modalActive: false
        };
    }

    componentWillMount() {
        const pathname = this.props.location.pathname;
        let type = 'add';
        if (/edit/.test(pathname)) {
            type = 'edit'
            this.getCertificate();
        }
        this.setState({ type })
    }

    componentDidUpdate() {
        console.log(this.state.modalActive)
        if (this.state.modalActive) {
            window.onbeforeunload = function () {
                return true;
            };
        } else {
            window.onbeforeunload = function () {
              
            };
        }
    }

    getCertificate = () => {
        const id = this.props.match.params.id;
        const promise = getCertificateById(id);
        promise.then(response => {
            this.setState({
                certificate: response.data.list[0],
                tags: response.data.list[0].tags,
                title: response.data.list[0].name,
                desc: response.data.list[0].description,
                price: response.data.list[0].price,
                duration: response.data.list[0].durationDays,
                certificateActive: response.data.list[0].active,
                titleError: true,
                descError: true,
                priceError: true,
                durationError: true
            })
        })
    }

    additionCertificate = () => {
        const name = this.state.title;
        const desc = this.state.desc;
        const price = this.state.price;
        const duration = this.state.duration;
        const tags = this.state.tags;
        const certificate = ({ name, desc, price, duration, tags })
        addCertificate(certificate).then(response => {
            window.location.reload()
            this.setState({
                successMessage: locale.getString('saveCertificate'),
                title: '',
                desc: '',
                price: '',
                duration: '',
                tags: [],
                titleError: false,
                descError: false,
                priceError: false,
                durationError: false,
                modalActive: false
            })
        }).catch(error => {
            this.setState({
                errorMessage: error.response.data.message
            })
        })
    }

    editCertificate = () => {
        const id = this.state.certificate.id
        const name = this.state.title;
        const desc = this.state.desc;
        const price = this.state.price;
        const duration = this.state.duration;
        const tags = this.state.tags;
        const active = this.state.certificateActive;
        const certificate = ({ id, name, desc, price, duration, tags, active });
        updateCertificate(certificate).then(response => {
            this.setState({ 
                successMessage: locale.getString('editCertificate'),
                modalActive: false
            })
        }).catch(error => {
            this.setState({ errorMessage: error.response.data.message })
        })
    }

    handleClick = () => {
        this.setState({ active: true })
    }

    handleClose = () => {
        this.setState({ active: false })
    }

    handleDeleteCertificate = () => {
        const id = this.state.certificate.id;
        deleteCertificate(id).then(response => {
            this.props.history.push('/certificates', ({ successMessage: locale.getString('deleteCertificate') }))
        }).catch(error => {
            this.setState({ errorMessage: error.response.data.message })
        })
    }

    handleChange = (e) => {
        this.setState({
            [e.target.name]: e.target.value,
            modalActive: true
        })
        const name = e.target.name;
        const value = e.target.value;
        switch (name) {
            case 'title':
                this.isValidTitle(value);
                break;
            case 'desc':
                this.isValidDesc(value);
                break;
            case 'price':
                this.isValidPrice(value);
                break;
            case 'duration':
                this.isValidDuration(value);
                break;
            case 'tagName':
                this.isValidTag(value);
                break;
        }
    }

    handleAddition = (e) => {
        e.preventDefault();
        let tags = this.state.tags;
        const tag = ({
            title: this.state.tagName
        });
        tags.push(tag);
        this.setState({
            tags: tags,
            tagName: ''
        })
    }

    handleDelete = (e) => {
        e.preventDefault();
        let tags = this.state.tags;
        const tagName = e.target.value;
        let tag = null;
        tags.map(local => {
            if (local.title == tagName) {
                tag = local;
            }
        })
        const index = tags.indexOf(tag);
        tags.splice(index, 1);
        this.setState({ tags })
    }

    isValidTitle = (value) => {
        const result = verifyTitle(value);
        this.setState({ titleError: result })
    }

    isValidDesc = (value) => {
        const result = verifyDescription(value);
        this.setState({ descError: result })
    }

    isValidDuration(value) {
        const result = verifyDuration(value);
        this.setState({ durationError: result })
    }

    isValidPrice(value) {
        const result = verifyPrice(value);
        this.setState({ priceError: result })
    }

    isValidTag(value) {
        const result = verifyTagName(value);
        this.setState({ tagNameError: result })
    }

    closeAlert = () => {
        this.setState({
            errorMessage: '',
            successMessage: ''
        })
        window.location.reload();
    }

    titleInput = () => {
        const { titleError, title } = this.state;
        return (
            <Row>
                <Col sm={3} />
                <Col sm={6}>
                    <Form.Label className="form-title">
                        {locale.getString('title')}
                    </Form.Label>
                    <Form.Control
                        title={titleError || title === '' ? locale.getString('titleMessage') : ''}
                        type="text" name="title" placeholder={locale.getString('enterTitle')}
                        value={title}
                        className="form-title-input"
                        onChange={this.handleChange}
                        maxLength={25}
                        isValid={titleError} isInvalid={!titleError && title != ''}
                    />
                    <Form.Label className={titleError || title === '' ? "form-counter" : "form-counter-error"}>
                        {title.length} / 25
                    </Form.Label>
                </Col>
                <Col sm={3} />
            </Row>
        )
    }

    descriptionInput = () => {
        const { descError, desc } = this.state;
        return (
            <Row>
                <Col sm={3} />
                <Col sm={6}>
                    <Form.Label className="form-desc">
                        {locale.getString('desc')}
                    </Form.Label>
                    <Form.Control
                        title={descError || desc === '' ? locale.getString('descMessage') : ''}
                        type="text" name="desc" placeholder={locale.getString('enterDesc')}
                        value={desc}
                        maxLength={240}
                        className="form-desc-input"
                        onChange={this.handleChange}
                        isValid={descError} isInvalid={!descError && desc != ''}
                    />
                    <Form.Label className={descError || desc === '' ? "form-counter" : "form-counter-error"}>
                        {desc.length} / 240
                </Form.Label>
                </Col>
                <Col sm={3} />
            </Row>
        )
    }

    priceInput = () => {
        const { priceError, price } = this.state;
        return (
            <Row>
                <Col sm={3} />
                <Col sm={6}>
                    <Form.Label className="form-price">
                        {locale.getString('price')}
                    </Form.Label>
                    <Form.Control
                        title={priceError || price === '' ? locale.getString('priceMessage') : ''}
                        type="text" name="price" placeholder={locale.getString('enterPrice')}
                        value={price}
                        maxLength={6}
                        className="form-price-input"
                        onChange={this.handleChange}
                        isValid={priceError} isInvalid={!priceError && price != ''}
                    />
                    <Form.Label >&nbsp;</Form.Label>
                </Col>
                <Col sm={3} />
            </Row>
        )
    }

    durationInput = () => {
        const { durationError, duration } = this.state;
        return (
            <Row>
                <Col sm={3} />
                <Col sm={6}>
                    <Form.Label className="form-duration">
                        {locale.getString('duration')}
                    </Form.Label>
                    <Form.Control
                        title={durationError || duration === '' ? locale.getString('durationMessage') : ''}
                        type="text" name="duration" placeholder={locale.getString('enterDuration')}
                        value={duration}
                        className="form-duration-input"
                        onChange={this.handleChange} maxlength={6}
                        isValid={durationError} isInvalid={!durationError && duration != ''}
                    />
                    <Form.Label>&nbsp;</Form.Label>
                </Col>
                <Col sm={3} />
            </Row>
        )
    }

    tagInput = () => {
        const { tagName, tagNameError } = this.state;
        return (
            <Row>
                <Col sm={3} />
                <Col sm={1}>
                    <button type="button"
                        className={
                            tagName === '' || tagNameError
                                ? "add-button-error"
                                : "add-button"}
                        onClick={this.handleAddition}>
                        +
                    </button>
                </Col>
                <Col sm={5}>
                    <Form.Label className="form-tag">
                        {locale.getString('tag')}
                    </Form.Label>
                    <Form.Control
                        title={tagNameError ? locale.getString('tagNameMessage') : ''}
                        type="text" name="tagName" placeholder={locale.getString('enterTag')}
                        value={tagName}
                        className="form-tag-input"
                        onChange={this.handleChange}
                        isValid={tagNameError && tagName != ''} isInvalid={!tagNameError}
                    />
                    <Form.Label>&nbsp;</Form.Label>
                </Col>
                <Col sm={3} />
            </Row>
        )
    }

    pageButtons = () => {
        const {
            type,
            title,
            desc,
            price,
            duration,
            titleError,
            descError,
            priceError,
            durationError,
            tagNameError,
        } = this.state;
        return (
            type === 'add'
                ?
                <Row>
                    <Col sm={5}>
                        <button type="button" className={
                            titleError && (title != '') &&
                                descError && (desc != '') &&
                                priceError && (price != '') &&
                                durationError && (duration != '') &&
                                !tagNameError
                                ? "button-item" : "button-item-disabled"}
                            onClick={this.additionCertificate}>
                            {locale.getString('add')}
                        </button>
                    </Col>
                </Row>
                :
                <Row>
                    <Col sm={6}>
                        <button type="button" className={
                            titleError &&
                                descError &&
                                priceError &&
                                durationError &&
                                tagNameError
                                ? "button-item" : "button-item-disabled"}
                            onClick={this.editCertificate}>
                            {locale.getString('save')}
                        </button>
                    </Col>
                    <Col sm={6}>
                        <button type="button" onClick={this.handleClick} className="button-item">
                            {locale.getString('delete')}
                        </button>
                        {this.state.active ?
                            <DeleteModal question={locale.getString('deleteQuestion')}
                                handleClose={this.handleClose}
                                handleDelete={this.handleDeleteCertificate} active={true} id={this.state.id} />
                            : ''}
                    </Col>
                </Row>
        )
    }

    render() {
        const {
            tags,
            errorMessage,
            successMessage
        } = this.state;
        return (
            <Container>
                <Row>
                    {successMessage != ''
                        ? <SuccessAlert close={this.closeAlert} message={successMessage} />
                        : errorMessage != ''
                            ? <ErrorAlert close={this.closeAlert} message={errorMessage} />
                            : <p className="empty-title">&nbsp;</p>}
                </Row>
                <Row>
                    <Col>
                        <PageTitle title={this.props.title} />
                    </Col>
                </Row>
                <Row>
                    <Col className="main-col">
                        <Container>
                            {this.titleInput()}
                            {this.descriptionInput()}
                            {this.priceInput()}
                            {this.durationInput()}
                            {this.tagInput()}
                            <Row>
                                <Col sm={3} />
                                <Col sm={2} className="tags">
                                    {tags.map(tag => <Tag handleDelete={this.handleDelete} title={tag.title} />)}
                                </Col>
                                <Col sm={3} />
                            </Row>
                            <Row className="buttons">
                                <Col sm={9} />
                                <Col sm={3} className="list-buttons">
                                    <Container>
                                        {this.pageButtons()}
                                    </Container>
                                </Col>
                            </Row>
                        </Container>
                    </Col>
                </Row>
            </Container>
        )
    }
}


export default withRouter(EditCertificate);