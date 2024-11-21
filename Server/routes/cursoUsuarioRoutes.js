
const express = require('express');
const cursoUsuarioController = require('../controllers/cursoUsuarioController');
const authMiddleware = require('../middlewares/authMiddleware'); 

const router = express.Router();


router.post('/inscribir', authMiddleware, cursoUsuarioController.inscribirUsuarioEnCurso);

router.get('/usuarios/:curso_id', authMiddleware, cursoUsuarioController.getUsuariosPorCurso);

router.get('/cursos/:usuario_id', authMiddleware, cursoUsuarioController.getCursosPorUsuario);
router.get('/cursosDetallados/:usuario_id', authMiddleware, cursoUsuarioController.getCursosPorUsuarioConDetalles); 

router.delete('/eliminar/:curso_id/:usuario_id', authMiddleware, cursoUsuarioController.eliminarInscripcion);

module.exports = router;
