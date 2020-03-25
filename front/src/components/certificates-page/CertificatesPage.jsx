import React from 'react';
import axios from 'axios';
import PageTitle from '../page-title/PageTitle';
import Certificate from './certificate/Certificate';
import './CertificatesPage.css';
import { Row } from 'react-bootstrap';
import { Col } from 'react-bootstrap';
import { Container } from 'react-bootstrap';
import { Form } from 'react-bootstrap';
import { Button, ButtonToolbar, ToggleButtonGroup, ToggleButton } from 'react-bootstrap';
import { withRouter } from 'react-router-dom'
import PaginationButton from '../pagination/PaginationButton'
import queryString from 'query-string'
import { getCertificates } from '../certificates-page/service/GetCertificates'
import { getUserCertificates } from '../certificates-page/service/GetUserCertificates'
import ErrorAlert from '../alerts/ErrorAlert'
import createTokenProvider from '../login/token/CreateTokenProvider'
import locale from '../header/language/Localization'
import SuccessAlert from '../alerts/SuccessAlert';
import PageSizeDropdown from './page-size-dropdown/PageSizeDropdown'

const tokenProvider = createTokenProvider();
const FIVE = '5';
const TWENTY_FIVE = '25';
const FIFTY = '50';
const HUNDRED = '100';
const roles = tokenProvider.getRoles();
const guest = roles.length === 0;

class CertificatesPage extends React.Component {
    constructor(props) {
        super(props);
        this.state = ({
            certificates: [],
            numberElements: null,
            pagination: null,
            type: 'all',
            pageNum: 1,
            pageSize: 5,
            search: '',
            partName: '',
            tags: [],
            filter: 'creation_date',
            successMessage: '',
            errorMessage: ''
        })
    }

    componentWillMount() {
        this.pageInit()
    }

    pageInit() {
        let type = queryString.parse(this.props.location.search).type;
        const filter = queryString.parse(this.props.location.search).sort;
        const roles = tokenProvider.getRoles();
        const guest = roles.length === 0;
        const state = this.props.location.state;
        const successMessage = state ? state.successMessage : '';
        this.createSearch()
        if (guest && type === 'my') {
            this.onHistoryUpdate();
            type = 'all'
        }
        this.setState({
            type: type === 'my' ? type : 'all',
            filter: filter ? filter : 'creation_date',
            successMessage: successMessage
        })
        if (type === 'my') {
            this.gettingPersonalCertificates();
        } else {
            this.gettingAllCertificates();
        }
    }

    createSearch = () => {
        const tempTags = queryString.parse(this.props.location.search).tagName;
        const tags = tempTags ? tempTags : [];
        const tempName = queryString.parse(this.props.location.search).partName;
        const partName = tempName ? tempName : '';
        let search = '';
        if (partName) {
            search += partName + ' ';
        }
        if (Array.isArray(tags)) {
            search += tags.map(tag => '#(' + tag + ')');
            search = search.replace(',', ' ');
        } else if (tags) {
            search += '#(' + tags + ')';
        }
        console.log(search)
        this.setState({
            search
        })
    }

    onHistoryUpdate() {
        this.props.history.push('/certificates')
    }

    componentWillReceiveProps(nextProps) {
        if (this.state.certificates != nextProps.certificates) {
            window.location.reload();
        }
    }

    gettingAllCertificates = (params = queryString.parse(this.props.location.search)) => {
        const promise = getCertificates(params);
        const pageSize = params.pageSize;
        this.checkPageSize(pageSize);
        promise.then(response => {
            const certificates = response.data.list;
            const numberElements = response.data.numberElements;
            const pagination = Math.ceil(numberElements / this.state.pageSize);
            this.setState({
                certificates: certificates,
                numberElements: numberElements,
                pagination: pagination
            })
        }).catch(error => {
            this.setState({ errorMessage: error.response.data.message })
        });
    }

    gettingPersonalCertificates = (params = queryString.parse(this.props.location.search)) => {
        const pageSize = params.pageSize;
        this.checkPageSize(pageSize);
        getUserCertificates(params).then(response => {
            const data = response.data;
            const boughtCertificates = data.boughtCertificates;
            const certificates = boughtCertificates.map(element => {
                let certificate = element.certificate;
                const price = element.price;
                certificate.price = price;
                return certificate;
            });
            const numberElements = data.numberElements;
            const pagination = Math.ceil(numberElements / this.state.pageSize);
            this.setState({
                certificates: certificates,
                numberElements: numberElements,
                pagination: pagination
            })
        }).catch(error => {
            this.setState({ errorMessage: error.response })
        });
    }

    searchChange = (e) => {
        const search = e.target.value;
        this.setState({ search })
    }

    searchHandle = (e) => {
        e.preventDefault();
        let parameters = queryString.parse(this.props.location.search);
        this.searchParser();
        const partName = this.state.partName;
        const tags = this.state.tags;
        const pageNum = undefined;
        const type = undefined
        parameters.pageNum = pageNum;
        parameters.type = type;
        partName ? parameters.partName = partName : delete parameters.partName;
        tags.length > 0 ? parameters.tagName = tags : delete parameters.tagName;
        this.props.history.push(this.props.location.pathname + '?' + queryString.stringify(parameters))
        this.gettingAllCertificates(parameters);
    }

    searchParser = () => {
        const search = this.state.search;
        const params = search.split(/[\s\,]/);
        let partName = '';
        let temp = [];
        let tags = [];
        params.forEach(param => {
            if (/^\w+$/.test(param)) {
                partName = param;
            } else if (/^#\(\w+\)$/.test(param)) {
                temp.push(param);
            }
        })
        temp.map(elem => {
            const tag = elem.replace(/[\(\)\#]/g, "")
            tags.push(tag);
        })
        this.state.partName = partName;
        this.state.tags = tags;
    }

    checkPageSize(pageSize) {
        switch (pageSize) {
            case FIVE:
                this.state.pageSize = FIVE;
                break;
            case TWENTY_FIVE:
                this.state.pageSize = TWENTY_FIVE;
                break;
            case FIFTY:
                this.state.pageSize = FIFTY;
                break;
            case HUNDRED:
                this.state.pageSize = HUNDRED;
                break;
            default:
                this.state.pageSize = 5;
        }
    }

    changeType = (e) => {
        const type = e.target.value;
        this.props.history.push(this.props.location.pathname + '?type=' + type)
        this.setState({
            type: type
        })
        if (type === 'my') {
            this.gettingPersonalCertificates();
        } else {
            this.gettingAllCertificates();
        }
    }

    changePageNum = (e) => {
        const pageNum = e.target.value;
        let parameters = queryString.parse(this.props.location.search);
        parameters.pageNum = pageNum;
        this.props.history.push(this.props.location.pathname + '?' + queryString.stringify(parameters))
        this.gettingAllCertificates(parameters);
    }

    changePageSize = (e) => {
        const pageSize = e.target.value;
        console.log(pageSize)
        let parameters = queryString.parse(this.props.location.search);
        parameters.pageSize = pageSize;
        parameters.pageNum = undefined;
        this.props.history.push(this.props.location.pathname + '?' + queryString.stringify(parameters))
        window.location.reload();
    }

    changeFilter = (e) => {
        const value = e.target.value;
        let parameters = queryString.parse(this.props.location.search);
        parameters.pageNum = undefined;
        parameters.sort = value;
        this.props.history.push(this.props.location.pathname + '?' + queryString.stringify(parameters))
        this.gettingAllCertificates(parameters);
        this.setState({ filter: value })
    }

    closeAlert = () => {
        this.setState({ errorMessage: '', successMessage: '' })
        this.props.history.push('/certificates')
    }

    buttonAllCertificates = () => {
        return (
            <Row className="certificates-item ml-auto mr-auto">
                <Col sm={12} className="certificates-item-div">
                    <Button value="all" onClick={this.changeType}
                        className={this.state.type === 'all' ? 'active certificate-button' : 'certificate-button'}>
                        {locale.getString('all')}
                    </Button>
                </Col>
            </Row>
        )
    }

    buttonMyCertificates = () => {
        return (
            !guest
                ?
                <Row className="certificates-item ml-auto mr-auto">
                    <Col sm={12} className="certificates-item-div">
                        <Button value="my" onClick={this.changeType}
                            className={this.state.type === 'my' ? 'active certificate-button' : 'certificate-button'}>
                            {locale.getString('my')}
                        </Button>
                    </Col>
                </Row>
                : ''
        )
    }

    filterButtons = () => {
        return (
            this.state.type === 'all'
                ?
                <Row className="filter">
                    <Col sm={5}>
                        <ButtonToolbar>
                            <ToggleButtonGroup type="radio" name="options" defaultValue={this.state.filter}>
                                <ToggleButton value="creation_date" onClick={this.changeFilter}>
                                    {locale.getString('date')} &#8593;
                                    </ToggleButton>
                                <ToggleButton value="name" onClick={this.changeFilter}>
                                    {locale.getString('title')} &#8593;
                                    </ToggleButton>
                                <ToggleButton value="description" onClick={this.changeFilter}>
                                    {locale.getString('desc')} &#8593;
                                    </ToggleButton>
                                <ToggleButton value="price" onClick={this.changeFilter}>
                                    {locale.getString('price')}  &#8593;
                                    </ToggleButton>
                            </ToggleButtonGroup>
                        </ButtonToolbar>
                    </Col>
                    <Col sm={2} />
                    <Col sm={5}>
                        <ButtonToolbar>
                            <ToggleButtonGroup type="radio" name="options" defaultValue={this.state.filter}>
                                <ToggleButton value="creation_date_desc" onClick={this.changeFilter}>
                                    {locale.getString('date')} &#8595;
                                    </ToggleButton>
                                <ToggleButton value="name_desc" onClick={this.changeFilter}>
                                    {locale.getString('title')} &#8595;
                                    </ToggleButton>
                                <ToggleButton value="description_desc" onClick={this.changeFilter}>
                                    {locale.getString('desc')} &#8595;
                                    </ToggleButton>
                                <ToggleButton value="price_desc" onClick={this.changeFilter}>
                                    {locale.getString('price')} &#8595;
                                    </ToggleButton>
                            </ToggleButtonGroup>
                        </ButtonToolbar>
                    </Col>
                </Row>
                : ''
        )
    }

    certificateCards = () => {
        return (
            this.state.certificates.map(certificate =>
                <Col sm={6}>
                    <Certificate
                        certificate={certificate}
                        id={certificate.id}
                        name={certificate.name}
                        date={certificate.creationDate}
                        desc={certificate.description}
                        price={certificate.price}
                        tags={certificate.tags}
                        active={certificate.active}
                    />
                </Col>
            )
        )
    }

    searchInput = () => {
        const strValue = this.state.search === '' ? locale.getString('search') + '...' : 'hello'
        return (
            <Form.Control type="search" placeholder={strValue} value={this.state.search}
                onChange={this.searchChange} />
        )
    }

    render() {
        return (
            <Container>
                <Row>
                    {this.state.successMessage != ''
                        ? <SuccessAlert close={this.closeAlert} message={this.state.successMessage} />
                        : <p className="empty-title">&nbsp;</p>}
                    {this.state.errorMessage != ''
                        ? <ErrorAlert close={this.closeAlert} message={this.state.errorMessage} />
                        : ''}
                </Row>
                <Row>
                    <Col>
                        <PageTitle title={locale.getString('certificates')} />
                    </Col>
                </Row>
                <Row>
                    <Col sm={2} className="myCertificates" >
                        <div className="type-certificates">
                            {this.buttonAllCertificates()}
                            {this.buttonMyCertificates()}
                        </div>
                    </Col>
                    <Col sm={10} className="search" >
                        <Form>
                            <Row>
                                <Col sm={10} className="search-input">
                                    {this.searchInput()}
                                </Col>
                                <Col sm={1} className="search-button">
                                    <Button type="submit" variant="outline-secondary" onClick={this.searchHandle}>
                                        {locale.getString('search')}
                                    </Button>
                                </Col>
                            </Row>
                        </Form>
                    </Col>
                </Row>
                {this.filterButtons()}
                <Row className="certificates">
                    {this.certificateCards()}
                </Row>
                <Row className="bottom-part">
                    <Col sm={2} className="page-size">
                        <PageSizeDropdown pageSize={this.state.pageSize} changePageSize={this.changePageSize} />
                    </Col>
                    <Col sm={1}></Col>
                    <Col sm={9} className="pagination">
                        {this.state.pagination ?
                            <PaginationButton numbers={this.state.pagination} gettingAllCertificates={this.gettingAllCertificates} /> : ''}
                    </Col>
                </Row>
            </Container>
        )
    }
}

export default withRouter(CertificatesPage);