import React from 'react'
import './PageTitle.css'

class PageTitle extends React.Component {
    render() {
        return (
            <div className="company-name">
                <h1>
                    {this.props.title}
                </h1>
            </div>
        )
    }
}

export default PageTitle;