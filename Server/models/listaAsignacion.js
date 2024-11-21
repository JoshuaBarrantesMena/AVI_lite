// models/ListaAsignacion.js
const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');

const ListaAsignacion = sequelize.define('lista_asignacion', {
    id_asignacion: {
        type: DataTypes.INTEGER,
        allowNull: false,
        references: {
            model: 'asignaciones',
            key: 'id_asignacion'
        },
        primaryKey: true,
    },
    id_estudiante: {
        type: DataTypes.INTEGER,
        allowNull: false,
        references: {
            model: 'usuario',
            key: 'identificacion'
        },
        primaryKey: true,
    },
    id_documento: {
        type: DataTypes.INTEGER,
        allowNull: true,
        references: {
            model: 'documentos',
            key: 'id_documento'
        },
        primaryKey: true,
    },
    calificado: {
        type: DataTypes.TINYINT.UNSIGNED.ZEROFILL,
        allowNull: true,
    },
    valor_obtenido: {
        type: DataTypes.INTEGER.UNSIGNED.ZEROFILL,
        allowNull: true,
    },
    fecha_entregado: {
        type: DataTypes.DATE,
        allowNull: true,
    },
    comentario: {
        type: DataTypes.STRING(300),
        allowNull: true,
    }
}, {
    tableName: 'lista_asignaciones',
    timestamps: false,
});

module.exports = ListaAsignacion;