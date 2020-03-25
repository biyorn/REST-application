import React from 'react'
import queryString from 'query-string'
import { Pagination } from 'react-bootstrap'
import { withRouter } from 'react-router-dom'
import './PaginationButton.css'

class PaginationButton extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            numbers: this.props.numbers
        }
    }

    changePageNum = (e) => {
        const pageNum = e.target.value;
        let parameters = queryString.parse(this.props.location.search);
        parameters.pageNum = pageNum;
        this.props.history.push(this.props.location.pathname + '?' + queryString.stringify(parameters))
        this.props.gettingAllCertificates(parameters);
    }

    render() {
        const params = queryString.parse(this.props.location.search);
        const pageNum = params.pageNum ? params.pageNum : 1;
        const first = 1;
        const last = this.state.numbers;
        const max = last <= 10 ? last : pageNum < 9 ? 10 : pageNum == last ? pageNum : +pageNum + 4;
        const min = last <= 10 ? 1 : pageNum < 9 ? 1 : +pageNum - 4;
        const prev = (pageNum - 1) === 0 ? 1 : pageNum - 1;
        const next = (+pageNum + 1) >= last ? last : +pageNum + +1;
        const pagination = [];
        for (let index = min; index <= max && index >= min; ++index) {
            pagination.push(
                <Pagination.Item value={index} as="button" onClick={this.changePageNum}
                    active={pageNum == index}>{index}</Pagination.Item>
            )
        }

        return (
            <Pagination>
                {first != pageNum ? <Pagination.Item value={first} as="button" onClick={this.changePageNum}>&laquo;</Pagination.Item> : ''}
                {first != pageNum ? <Pagination.Item value={prev} as="button" onClick={this.changePageNum}>&#8249;</Pagination.Item> : ''}
                {pagination}
                {last != pageNum ? <Pagination.Item value={next} as="button" onClick={this.changePageNum}>&#8250;</Pagination.Item> : ''}
                {last != pageNum ? <Pagination.Item value={last} as="button" onClick={this.changePageNum}>&raquo;</Pagination.Item> : ''}
            </Pagination>
        )
    }
}

export default withRouter(PaginationButton);