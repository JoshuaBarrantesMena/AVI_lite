
const express = require('express');
const cursoController = require('../controllers/cursoController');
const authMiddleware = require('../middlewares/authMiddleware'); 

const router = express.Router();


router.post('/', authMiddleware, cursoController.createCurso);
router.get('/', authMiddleware, cursoController.getAllCursos);
router.get('/:id', authMiddleware, cursoController.getCursoById);
router.get('/universidad/:id', authMiddleware, cursoController.getCursosByUniversity); 
router.get('/busqueda/universidad/:id/:nombre/:profesor_id', cursoController.getCursosByUniversityAndName);


router.get('/carrera/:id', authMiddleware, cursoController.getCoursesByCareer); 
router.get('/profesor/:id', authMiddleware, cursoController.getCoursesByTeacher); //
router.put('/:id', authMiddleware, cursoController.updateCurso);
router.delete('/:id', authMiddleware, cursoController.deleteCurso);

module.exports = router;
