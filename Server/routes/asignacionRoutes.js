
const express = require('express');
const asignacionController = require('../controllers/asignacionController');
const authMiddleware = require('../middlewares/authMiddleware'); 

const router = express.Router();


router.post('/', authMiddleware, asignacionController.createAsignacion);

router.get('/', authMiddleware, asignacionController.getAllAsignaciones);
router.get('/curso/:id', authMiddleware, asignacionController.getAllAsignacionesByCourse); 

router.get('/:id', authMiddleware, asignacionController.getAsignacionById);

router.put('/:id', authMiddleware, asignacionController.updateAsignacion);

router.delete('/:id', authMiddleware, asignacionController.deleteAsignacion);

module.exports = router;
