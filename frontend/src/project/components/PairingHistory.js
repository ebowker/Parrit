import React from 'react'
import PropTypes from 'prop-types'
import exact from 'prop-types-exact'
import { connect } from 'react-redux'
import { Scrollbars } from 'react-custom-scrollbars'
import classNames from 'classnames'

import { getPairingHistoryThunk } from '../actions/thunks/DataThunks.js'
import { setPairingHistoryPanelOpenCreator } from '../actions/creators/SettingsCreators'
import PairingHistoryRecordList from './PairingHistoryRecordList.js'

class PairingHistory extends React.Component {
    componentDidMount() {
        this.props.fetchPairingHistory()
    }

    render() {
        const {pairingHistoryList} = this.props

        const classes = classNames({
            'pairing-history-panel': true,
            'panel-open': this.props.isPairingHistoryPanelOpen,
            'panel-closed': !this.props.isPairingHistoryPanelOpen
        })

        return (
            <div className={classes}>
                <Scrollbars>
                    <div className="inner-pairing-history-wrapper">
                        <div className="header">
                            <h2>Pair Rotation History</h2>
                            <div className="cancel" onClick={this.closePairingHistoryPanel.bind(this)}/>
                        </div>

                        <div className="body">
                            {pairingHistoryList.length === 0 && (
                                <div className="no-history">
                                    <div className="clock"/>
                                    <div className="no-history-content">
                                        ‘Record Pairs’ to track daily rotation history. The more you record, the better the recommendation engine becomes.
                                    </div>
                                </div>
                            )}

                            {pairingHistoryList.length > 0 && (
                                <PairingHistoryRecordList pairingHistoryList={pairingHistoryList}/>
                            )}
                        </div>
                    </div>
                </Scrollbars>
            </div>
        )
    }

    closePairingHistoryPanel() {
        this.props.setPairingHistoryPanelOpen(false)
    }
}

PairingHistory.propTypes = exact({
    pairingHistoryList: PropTypes.arrayOf(PropTypes.object).isRequired,
    isPairingHistoryPanelOpen: PropTypes.bool.isRequired,
    fetchPairingHistory: PropTypes.func.isRequired,
    setPairingHistoryPanelOpen: PropTypes.func.isRequired
})

function mapStateToProps({data, settings}) {
    return {
        pairingHistoryList: data.pairingHistory.pairingHistoryList,
        isPairingHistoryPanelOpen: settings.pairingHistoryPanel.isOpen
    }
}

const mapDispatchToProps = {
    fetchPairingHistory: getPairingHistoryThunk,
    setPairingHistoryPanelOpen: setPairingHistoryPanelOpenCreator
}

export default connect(mapStateToProps, mapDispatchToProps)(PairingHistory)
