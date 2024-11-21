// models/Ia.js
const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');

const IA = sequelize.define('ia', {
    id_ia: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
    },
    valor: {
        type: DataTypes.INTEGER.UNSIGNED.ZEROFILL,
        allowNull: false,
    },
    respuesta: {
        type: DataTypes.STRING(200),
        allowNull: false,
    }
}, {
    tableName: 'ia',
    timestamps: false,
});

module.exports = IA;