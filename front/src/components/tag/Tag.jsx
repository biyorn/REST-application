import React from 'react'
import { Button } from 'react-bootstrap'
import './Tag.css'
import queryString from 'query-string'
import { withRouter } from 'react-router-dom'
import { getCertificates } from '../certificates-page/service/GetCertificates'

class Tag extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            title: this.props.title
        }
    }

    handleClick = () => {
        let tags = []
        tags.push(this.state.title)
        const params = ({ tagName: tags })
        this.props.history.push('/certificates' + '?' + queryString.stringify(params))
        getCertificates(params);
    }

    render() {
        return (
            <Button variant="success" className="Button" onClick={this.handleClick}>{this.props.title}</Button>
        )
    }
}

export default withRouter(Tag);