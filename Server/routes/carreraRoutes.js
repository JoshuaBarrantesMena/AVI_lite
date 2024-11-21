
const express = require('express');
const carreraController = require('../controllers/carreraController');
const authMiddleware = require('../middlewares/authMiddleware'); 

const router = express.Router();


router.post('/', authMiddleware, carreraController.createCarrera);

router.get('/', authMiddleware, carreraController.getAllCarreras);


router.get('/departamento/:departamento_id', authMiddleware,carreraController.getCareerByDepartment);
router.get('/universidad/:id', authMiddleware, carreraController.getCareerByUniversity); 
router.get('/:id', authMiddleware, carreraController.getCarreraById);

router.put('/:id', authMiddleware, carreraController.updateCarrera);

router.delete('/:id', authMiddleware, carreraController.deleteCarrera);

module.exports = router;
