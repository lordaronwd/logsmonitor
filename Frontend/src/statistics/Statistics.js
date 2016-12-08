import React, { Component } from 'react';
import './Statistics.css';

class Statistics extends Component {

  constructor() {
    super();
    this.state = {
      errors: 0,
      warnings: 0,
      infos: 0
    };
  }

  componentDidMount() {
    this.setState({
      errors: this.props.errors,
      warnings: this.props.warnings,
      infos: this.props.infos
    });
  }

  componentWillReceiveProps(nextProps) {
    this.setState({
      errors: nextProps.errors,
      warnings: nextProps.warnings,
      infos: nextProps.infos
    });
  }

  render() {
    return (
      <div className="statistics">
      <table>
       <tbody>
          <tr>
            <td>Errors:</td>
            <td className="counter-value"><span className="odometer">{this.state.errors}</span></td>
          </tr>
           <tr>
            <td>Warnings:</td>
            <td className="counter-value"><span className="odometer">{this.state.warnings}</span></td>
          </tr>
           <tr>
            <td>Infos:</td>
            <td className="counter-value"><span className="odometer">{this.state.infos}</span></td>
          </tr>
        </tbody>
      </table>
      </div>
    );
  }
}

Statistics.propTypes = {
  errors: React.PropTypes.number.isRequired,
  warnings: React.PropTypes.number.isRequired,
  infos: React.PropTypes.number.isRequired,
}

export default Statistics;
