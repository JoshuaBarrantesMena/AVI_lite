// models/Documento.js
const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');

const Documento = sequelize.define('documento', {
    id_documento: {
        type: DataTypes.INTEGER,
        primaryKey: true,
        autoIncrement: true,
        allowNull: false,
    },
    id_estudiante: {
        type: DataTypes.INTEGER,
        allowNull: false,
        references: {
            model: 'usuario',
            key: 'identificacion'
        }
    },
    url: {
        type: DataTypes.STRING(60),
        allowNull: false,
    },
    hash_: {
        type: DataTypes.STRING(32),
        allowNull: false,
    }
}, {
    tableName: 'documentos',
    timestamps: false,
});

module.exports = Documento;