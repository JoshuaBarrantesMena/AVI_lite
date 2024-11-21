

const express = require('express');
const listaAsignacionController = require('../controllers/listaAsignacionController');
const authMiddleware = require('../middlewares/authMiddleware'); 

const router = express.Router();


router.post('/', authMiddleware, listaAsignacionController.createListaAsignacion);

router.get('/', authMiddleware, listaAsignacionController.getAllListaAsignaciones);

router.get('/:id_asignacion', authMiddleware, listaAsignacionController.getListaAsignacionById);
router.get('/estudiante/:id_estudiante', authMiddleware, listaAsignacionController.getAllListaAsignacionesEstudiantes);
router.put('/:id_asignacion', authMiddleware, listaAsignacionController.updateListaAsignacion);

router.delete('/:id_asignacion', authMiddleware, listaAsignacionController.deleteListaAsignacion);

module.exports = router;

