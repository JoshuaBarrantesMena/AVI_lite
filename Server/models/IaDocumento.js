// models/IaDocumento.js
const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');

const IaDocumento = sequelize.define('ia_documento', {
    id_ia: {
        type: DataTypes.INTEGER,
        allowNull: false,
        references: {
            model: 'ia',
            key: 'id_ia'
        },
        primaryKey: true,
    },
    id_documento: {
        type: DataTypes.INTEGER,
        allowNull: false,
        references: {
            model: 'documentos',
            key: 'id_documento'
        },
        primaryKey: true,
    }
}, {
    tableName: 'ia_documento',
    timestamps: false
});

module.exports = IaDocumento;