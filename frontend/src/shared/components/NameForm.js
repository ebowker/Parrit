import React from 'react'
import PropTypes from 'prop-types'
import exact from 'prop-types-exact'

class NameForm extends React.Component {
    constructor(props) {
        super(props)
        this.state = { name: '' }
    }

    render() {
        let inputClasses = 'form-control'
        inputClasses += this.props.errorMessage ? ' error': ''

        return (
            <form onSubmit={this.submit.bind(this)}>
                <div className="form-header">
                    <h2 className="form-title">{this.props.formTitle}</h2>
                    <div className="form-cancel" onClick={this.props.cancelFunction}/>
                </div>

                <div className="error-message">{this.props.errorMessage}</div>

                <input
                    className={inputClasses}
                    autoFocus
                    type="text"
                    placeholder="Name"
                    value={this.state.name}
                    onChange={this.handleChange.bind(this)}
                />

                <div className="buttons">
                    <button type="submit" className="button-blue">OK</button>
                    <button type="button" onClick={this.props.cancelFunction} className="button-red">Cancel</button>
                </div>
            </form>
        )
    }

    handleChange(event) {
        this.setState({name: event.target.value})
    }

    submit(e) {
        e.preventDefault()
        this.props.confirmFunction(this.state.name)
    }
}

NameForm.propTypes = exact({
    formTitle: PropTypes.string.isRequired,
    confirmFunction: PropTypes.func.isRequired,
    cancelFunction: PropTypes.func.isRequired,
    errorMessage: PropTypes.string
})

export default NameForm
