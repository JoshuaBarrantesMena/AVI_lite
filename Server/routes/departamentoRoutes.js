
const express = require('express');
const departamentoController = require('../controllers/departamentoController');
const authMiddleware = require('../middlewares/authMiddleware'); 

const router = express.Router();


router.post('/', authMiddleware, departamentoController.createDepartamento);

router.get('/', authMiddleware, departamentoController.getAllDepartamentos);

router.get('/:id', authMiddleware, departamentoController.getDepartamentoById);
router.get('/university/:id', authMiddleware, departamentoController.getByUniversity);
router.put('/:id', authMiddleware, departamentoController.updateDepartamento);

router.delete('/:id', authMiddleware, departamentoController.deleteDepartamento);

module.exports = router;
