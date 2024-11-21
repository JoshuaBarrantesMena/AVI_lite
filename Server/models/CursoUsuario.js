// models/CursoUsuario.js
const { DataTypes } = require('sequelize');
const sequelize = require('../config/database');

const CursoUsuario = sequelize.define('curso_usuario', {
    curso_id: {
        type: DataTypes.INTEGER,
        allowNull: false,
        references: {
            model: 'cursos',
            key: 'curso_id'
        },
        primaryKey: true,
    },
    usuario_id: {
        type: DataTypes.INTEGER,
        allowNull: false,
        references: {
            model: 'usuario',
            key: 'identificacion'
        },
        primaryKey: true,
    }
}, {
    tableName: 'cursos_usuarios',
    timestamps: false
});

module.exports = CursoUsuario;