// models/Facultad.js
const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');

const Facultad = sequelize.define('facultades', {
    facultad_id: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
    },
    universidad_id: {
        type: DataTypes.INTEGER,
        allowNull: false,
        references: {
            model: 'universidades',
            key: 'universidad_id'
        }
    },
    nombre: {
        type: DataTypes.STRING(100),
        allowNull: false,
    },
}, {
    tableName: 'facultades',
    timestamps: false,
});

module.exports = Facultad;